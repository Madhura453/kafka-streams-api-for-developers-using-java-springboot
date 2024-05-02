package com.kafka.orderskafkastreamsapp.topology;

import com.kafka.orderskafkastreamsapp.domain.*;
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
public class JoinInOrdersTopology {
    private static String ordersSourceTopic = "orders";

    private static String STORES_TOPIC = "stores";
    public static final String GENERAL_ORDERS_COUNT_STORE = "general_orders_count";
    public static final String GENERAL_ORDERS_REVENUE_STORE = "general_orders_revenue";

    public static final String ORDERS_REVENUE_LABEL = "orders_revenue";

    public static final String ORDERS_COUNT_LABEL = "orders_count";

    public static final String RESTAURANT_ORDERS_COUNT_STORE = "restaurant_orders_count";
    public static final String RESTAURANT_ORDERS_REVENUE_STORE = "restaurant_orders_revenue";

    Predicate<String, Order> generalPredicate = (key, order) -> order.orderType().equals(OrderType.GENERAL);
    Predicate<String, Order> restaruntPredicate = (key, order) -> order.orderType().equals(OrderType.RESTAURANT);

    public static Topology buildTopology() {


        Predicate<String, Order> generalPredicate = (key, order) -> order.orderType().equals(OrderType.GENERAL);

        Predicate<String, Order> restaruntPredicate = (key, order) -> order.orderType().equals(OrderType.RESTAURANT);

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Order> orderStreams = streamsBuilder.stream(ordersSourceTopic,
                Consumed.with(Serdes.String(), SerdesFactory.orderSerdes()));
        orderStreams
                .print(Printed.<String, Order>toSysOut().withLabel("orders"));

        log.info("fetching records from store topic. Creating KTable for store records");

        KTable<String, Store> storesTable =
                streamsBuilder.table(STORES_TOPIC, Consumed.with(Serdes.String(), SerdesFactory.storeSerdes()));
        storesTable
                .toStream()
                .print(Printed.<String, Store>toSysOut().withLabel("stores_table"));

        orderStreams.split(Named.as("General_restaurant_stream")) //// General_restaurant_stream was any name we can give
                .branch(generalPredicate, Branched.withConsumer(generalOrderStream -> {
                    log.info("count the general orders based on key location id");
                    joinAggregateOrdersByCountAndStore(generalOrderStream, GENERAL_ORDERS_COUNT_STORE, storesTable);
                    joinAggregateOrdersByRevenueAndStore(generalOrderStream, GENERAL_ORDERS_REVENUE_STORE, storesTable);

                }))
                .branch(restaruntPredicate, Branched.withConsumer(restaurantOrderStream ->
                {
                    log.info("count the restaurant orders based on location id");
                    joinAggregateOrdersByCountAndStore(restaurantOrderStream, RESTAURANT_ORDERS_COUNT_STORE, storesTable);
                  //  joinAggregateOrdersByRevenueAndStore(restaurantOrderStream, RESTAURANT_ORDERS_REVENUE_STORE, storesTable);
                }));
        return streamsBuilder.build();
    }

    private static void joinAggregateOrdersByCountAndStore(KStream<String, Order> orderStream, String countStoreName,
                                                           KTable<String, Store> storesTable) {
        log.info("grouping the orders based on location id. Here key is location id");

        KTable<String, Long> orderCountPerStore = orderStream.map((key, value) -> KeyValue.pair(value.locationId(), value))
                .groupByKey(Grouped.with(Serdes.String(), SerdesFactory.orderSerdes()))
                .count(Named.as(countStoreName), Materialized.as(countStoreName));

        orderCountPerStore.toStream().print(Printed.<String, Long>toSysOut().withLabel(countStoreName));

        ValueJoiner<Long, Store, TotalCountWithAddress> valueJoiner = TotalCountWithAddress::new;

        KTable<String,TotalCountWithAddress> totalCountWithStoreTable = orderCountPerStore
                .join(storesTable,valueJoiner);

        totalCountWithStoreTable.toStream().print(Printed.<String,TotalCountWithAddress>toSysOut().
                withLabel(countStoreName+"-by Store"));
        /*
[general_orders_count]: store_4567, 6
[general_orders_count-by Store]: store_4567, TotalCountWithAddress[ordersCount=6, store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[general_orders_revenue]: store_4567, TotalRevenue[locationId=store_4567, runnuingOrderCount=7, runningRevenue=189.00]
[general_orders_revenue-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=7, runningRevenue=189.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[restaurant_orders_count]: store_4567, 6
[restaurant_orders_count-by Store]: store_4567, TotalCountWithAddress[ordersCount=6, store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[general_orders_count]: store_1234, 6
[general_orders_count-by Store]: store_1234, TotalCountWithAddress[ordersCount=6, store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[general_orders_revenue]: store_1234, TotalRevenue[locationId=store_1234, runnuingOrderCount=7, runningRevenue=189.00]
[general_orders_revenue-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=7, runningRevenue=189.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[restaurant_orders_count]: store_1234, 6
[restaurant_orders_count-by Store]: store_1234, TotalCountWithAddress[ordersCount=6, store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
         */
    }

    private static void joinAggregateOrdersByRevenueAndStore(KStream<String, Order> orderStream, String aggregatorStoreName,
                                                 KTable<String, Store> storesTable) {

        log.info("creating first initializer to store the aggregator information");
        Initializer<TotalRevenue> totalRevenueInitializer = TotalRevenue::new;
        log.info("creating aggregator key is string value is order aggregator result is TotalRevenue");
        Aggregator<String, Order, TotalRevenue> aggregator = (key, value, aggregate) -> aggregate.updateRunningRevenue(key, value);
        log.info("call to aggregator function");
        KTable<String, TotalRevenue> revenueTable = orderStream
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
                .print(Printed.<String, TotalRevenue>toSysOut().withLabel(aggregatorStoreName));

        log.info("TotalRevenue result is Ktable(TotalRevenue) and Store result is KTable(Store). " +
                "So join will be happen on KTable-KTable and result will be TotalRevenueWithAddress");

        ValueJoiner<TotalRevenue, Store, TotalRevenueWithAddress> valueJoiner = TotalRevenueWithAddress::new;

        KTable<String,TotalRevenueWithAddress> revenueWithStoreTable = revenueTable.join(storesTable,valueJoiner);

        revenueWithStoreTable.toStream().print(Printed.<String,TotalRevenueWithAddress>toSysOut().
                withLabel(aggregatorStoreName+"-by Store"));

       /*
       [stores_table]: store_4567, Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]
[general_orders_revenue-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[restaurant_orders_revenue-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[stores_table]: store_3456, Store[locationId=store_3456, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234563333]
[stores_table]: store_4390, Store[locationId=store_4390, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=098733333333]
[general_orders_revenue]: store_4567, TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00]
[general_orders_revenue-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[restaurant_orders_revenue]: store_4567, TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00]
[restaurant_orders_revenue-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[stores_table]: store_1234, Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]
[general_orders_revenue-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=27.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[restaurant_orders_revenue-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=15.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[general_orders_revenue]: store_1234, TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=27.00]
[general_orders_revenue-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=27.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[restaurant_orders_revenue]: store_1234, TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=15.00]
[restaurant_orders_revenue-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=15.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
        */
    }
}
