package com.kafka.orderskafkastreamsapp.topology;

import com.kafka.orderskafkastreamsapp.domain.Order;
import com.kafka.orderskafkastreamsapp.domain.OrderType;
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
public class ReKeyingKafkaRecordsTopology {
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
        // copied code from AggregationInOrdersTopology
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

                }))
                .branch(restaruntPredicate, Branched.withConsumer(restaurantOrderStream ->
                {
                    log.info("count the restaurant orders based on location id");
                    aggregateOrdersByCount(restaurantOrderStream, RESTAURANT_ORDERS_COUNT_STORE);
                }));
        return streamsBuilder.build();
    }

    private static void aggregateOrdersByCount(KStream<String, Order> orderStream, String countStoreName) {
        log.info("grouping the orders based on location id. Here key is location id");
        log.info("map operation take cares of adding new key");
        KTable<String, Long> orderCountPerStore = orderStream
                .map((key, value) -> KeyValue.pair(value.locationId(), value))
                .groupByKey(Grouped.with(Serdes.String(), SerdesFactory.orderSerdes()))
                .count(Named.as(countStoreName), Materialized.as(countStoreName));

        orderCountPerStore.toStream().print(Printed.<String, Long>toSysOut().withLabel(countStoreName));

    }



}
