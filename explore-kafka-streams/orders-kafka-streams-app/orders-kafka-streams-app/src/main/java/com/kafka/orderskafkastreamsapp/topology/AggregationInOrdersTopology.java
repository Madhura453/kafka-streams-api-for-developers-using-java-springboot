package com.kafka.orderskafkastreamsapp.topology;

import com.kafka.orderskafkastreamsapp.domain.Order;
import com.kafka.orderskafkastreamsapp.domain.OrderType;
import com.kafka.orderskafkastreamsapp.domain.Revenue;
import com.kafka.orderskafkastreamsapp.domain.TotalRevenue;
import com.kafka.orderskafkastreamsapp.serdes.SerdesFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

@Slf4j
public class AggregationInOrdersTopology {

    private static String ordersSourceTopic = "orders";
    public static final String GENERAL_ORDERS_TOPIC = "general_orders";
    public static final String GENERAL_ORDERS_COUNT_STORE = "general_orders_count_store";
    public static final String GENERAL_ORDERS_REVENUE_STORE = "general_orders_revenue_store";

    public static final String RESTAURANT_ORDERS_TOPIC = "restaurant_orders";
    public static final String RESTAURANT_ORDERS_COUNT_STORE = "restaurant_orders_count_store";
    public static final String RESTAURANT_ORDERS_REVENUE_STORE = "restaurant_orders_revenue_store";

    Predicate<String, Order> generalPredicate = (key, order) -> order.orderType().equals(OrderType.GENERAL);
    Predicate<String, Order> restaruntPredicate = (key, order) -> order.orderType().equals(OrderType.RESTAURANT);

    public static Topology buildTopology() {
        /*
        1) Split the orders 2 types 1) general 2) restaurant
        2) convert to key is location id and value is order
        3) count the orders based on location I'd. Here is key is id
         */

        Predicate<String, Order> generalPredicate = (key, order) -> order.orderType().equals(OrderType.GENERAL);

        Predicate<String, Order> restaruntPredicate = (key, order) -> order.orderType().equals(OrderType.RESTAURANT);

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Order> orderStreams = streamsBuilder.stream(ordersSourceTopic,
                Consumed.with(Serdes.String(), SerdesFactory.orderSerdes()));
        orderStreams
                .print(Printed.<String, Order>toSysOut().withLabel("orders"));

        orderStreams.split(Named.as("General_restaurant_stream")) //// General_restaurant_stream was any name we can give
                .branch(generalPredicate, Branched.withConsumer(generalOrderStream -> {
                    log.info("count the general orders based on key location id");
                    aggregateOrdersByCount(generalOrderStream, GENERAL_ORDERS_COUNT_STORE);
                    aggregateOrdersByRevenue(generalOrderStream, GENERAL_ORDERS_REVENUE_STORE);

                }))
                .branch(restaruntPredicate, Branched.withConsumer(restaurantOrderStream ->
                {
                    log.info("count the restaurant orders based on location id");
                    aggregateOrdersByCount(restaurantOrderStream, RESTAURANT_ORDERS_COUNT_STORE);
                    aggregateOrdersByRevenue(restaurantOrderStream, RESTAURANT_ORDERS_REVENUE_STORE);
                }));
        return streamsBuilder.build();
    }

    private static void aggregateOrdersByCount(KStream<String, Order> orderStream, String countStoreName) {
        log.info("grouping the orders based on location id. Here key is location id");

        KTable<String, Long> orderCountPerStore = orderStream.map((key, value) -> KeyValue.pair(value.locationId(), value))
                .groupByKey(Grouped.with(Serdes.String(), SerdesFactory.orderSerdes()))
                .count(Named.as(countStoreName), Materialized.as(countStoreName));

        orderCountPerStore.toStream().print(Printed.<String, Long>toSysOut().withLabel(countStoreName));
    /*
    o/p = [restaurant_orders_count_store]: store_4567, 4
          [restaurant_orders_count_store]: store_1234, 4
          [general_orders_count_store]: store_1234, 4
          [general_orders_count_store]: store_4567, 4
     */
    }

    private static void aggregateOrdersByRevenue(KStream<String, Order> orderStream, String aggregatorStoreName) {
        /*
        1) So in the order type, we are trying to do here is that so for each location we are going to continuously
aggregate the total amount that's been received on each and every event.
So in this case, each order is going to have a location ID and then final amount if the location ID is the same for a given event,
in that case, we are going to be adding the final amount to the previously calculated final amount.
         */
        /*
         *  aggregate steps
         * 1) create initializer 2) aggregator 3) aggregate call
         */
        log.info("creating first initializer to store the aggregator information");
        Initializer<TotalRevenue> totalRevenueInitializer = TotalRevenue::new;
        log.info("creating aggregator key is string value is order aggregator result is TotalRevenue");
        Aggregator<String, Order, TotalRevenue> aggregator = (key, value, aggregate) -> aggregate.updateRunningRevenue(key, value);
        log.info("call to aggregator function");
      KTable<String, TotalRevenue> revenueTable=  orderStream
                .map((key, value) -> KeyValue.pair(value.locationId(), value))
                //converting key is location id and value is order
                .groupByKey(Grouped.with(Serdes.String(), SerdesFactory.orderSerdes()))
                //group the records based on key i.e location id
                .aggregate(totalRevenueInitializer,
                        aggregator,
                        Materialized.<String, TotalRevenue, KeyValueStore<Bytes, byte[]>>as(aggregatorStoreName)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(SerdesFactory.totalRevenueSerdes()));


      revenueTable.toStream()
              .print(Printed.<String,TotalRevenue>toSysOut().withLabel(aggregatorStoreName));

        /*
        o/p=
        [general_orders_revenue_store]: store_4567, TotalRevenue[locationId=store_4567, runnuingOrderCount=7, runningRevenue=189.00]
       [restaurant_orders_revenue_store]: store_1234, TotalRevenue[locationId=store_1234, runnuingOrderCount=7, runningRevenue=105.00]

         */
        /*
        Any time we changed key for record kafka internally do the partition by internal topic
         */
    }
}
