package kafkastreams.ordersmanagementstreams.topology;


import kafkastreamsbase.ordersdomain.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Component
@Slf4j
public class OrdersTopology {

    public static String ORDERS_TOPIC = "orders";
    public static String STORES_TOPIC = "stores";
    public static final String GENERAL_ORDERS = "general_orders";
    public static final String GENERAL_ORDERS_COUNT_WINDOWS_STORE = "general_orders_count_window";
    public static final String GENERAL_ORDERS_REVENUE_WINDOWS_STORE = "general_orders_revenue_window";
    public static final String GENERAL_ORDERS_COUNT = "general_orders_count";
    public static final String GENERAL_ORDERS_REVENUE = "general_orders_revenue";

    public static final String RESTAURANT_ORDERS_COUNT = "restaurant_orders_count";
    public static final String RESTAURANT_ORDERS_COUNT_WINDOWS_STORE = "restaurant_orders_count_window";
    public static final String RESTAURANT_ORDERS_REVENUE_WINDOWS_STORE = "restaurant_orders_revenue_window";
    public static final String RESTAURANT_ORDERS_REVENUE = "restaurant_orders_revenue";
    public static final String RESTAURANT_ORDERS = "restaurant_orders";


    @Autowired
    public void process(StreamsBuilder streamsBuilder) {

        orderTopology(streamsBuilder);

    }

    private  void orderTopology(StreamsBuilder streamsBuilder) {

        Predicate<String, Order> generalPredicate = (key, order) -> order.orderType().equals(OrderType.GENERAL);

        Predicate<String, Order> restaruntPredicate = (key, order) -> order.orderType().equals(OrderType.RESTAURANT);

        KStream<String, Order> orderStreams = streamsBuilder.stream(ORDERS_TOPIC,
                Consumed.with(Serdes.String(), new JsonSerde<>(Order.class)));
        orderStreams
                .print(Printed.<String, Order>toSysOut().withLabel("orders"));

        log.info("fetching records from store topic. Creating KTable for store records");

        KTable<String, Store> storesTable =
                streamsBuilder.table(STORES_TOPIC, Consumed.with(Serdes.String(), new JsonSerde<>(Store.class)));
        storesTable
                .toStream()
                .print(Printed.<String, Store>toSysOut().withLabel("stores_table"));

        orderStreams.split(Named.as("General_restaurant_stream")) //// General_restaurant_stream was any name we can give
                .branch(generalPredicate, Branched.withConsumer(generalOrderStream -> {
                    aggregateOrdersByCount(generalOrderStream,GENERAL_ORDERS_COUNT,storesTable);
                    aggregateOrdersCountByTimeWindows(generalOrderStream, GENERAL_ORDERS_COUNT_WINDOWS_STORE, storesTable);
                    aggregateOrdersByRevenue(generalOrderStream,GENERAL_ORDERS_REVENUE,storesTable);
                    aggregateOrdersByRevenueByTimeWindows(generalOrderStream, GENERAL_ORDERS_REVENUE_WINDOWS_STORE, storesTable);

                }))
                .branch(restaruntPredicate, Branched.withConsumer(restaurantOrderStream ->
                {
                    aggregateOrdersByCount(restaurantOrderStream,RESTAURANT_ORDERS_COUNT,storesTable);
                    aggregateOrdersCountByTimeWindows(restaurantOrderStream, RESTAURANT_ORDERS_COUNT_WINDOWS_STORE, storesTable);
                    aggregateOrdersByRevenue(restaurantOrderStream,RESTAURANT_ORDERS_REVENUE,storesTable);
                    aggregateOrdersByRevenueByTimeWindows(restaurantOrderStream, RESTAURANT_ORDERS_REVENUE_WINDOWS_STORE, storesTable);
                }));

    }


    private void  aggregateOrdersByCount(KStream<String, Order> orderStream, String countStoreName,
                                                           KTable<String, Store> storesTable) {
        log.info("grouping the orders based on location id. Here key is location id");

        KTable<String, Long> orderCountPerStore = orderStream.map((key, value) -> KeyValue.pair(value.locationId(), value))
                .groupByKey(Grouped.with(Serdes.String(), new JsonSerde<>(Order.class)))
                .count(Named.as(countStoreName), Materialized.as(countStoreName));

        orderCountPerStore.toStream().print(Printed.<String, Long>toSysOut().withLabel(countStoreName));

        ValueJoiner<Long, Store, TotalCountWithAddress> valueJoiner = TotalCountWithAddress::new;

        KTable<String,TotalCountWithAddress> totalCountWithStoreTable = orderCountPerStore
                .join(storesTable,valueJoiner);

        totalCountWithStoreTable.toStream().print(Printed.<String,TotalCountWithAddress>toSysOut().
                withLabel(countStoreName+"-by Store"));
    }

    private void aggregateOrdersCountByTimeWindows(KStream<String, Order> orderStream, String storeName,
                                                   KTable<String, Store> storesTable)
    {
        /*
        without Grace period  logic
        Duration windowSize = Duration.ofSeconds(15);
         TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(windowSize);
         */
        Duration windowSize = Duration.ofSeconds(60);
        Duration graceWindowSize = Duration.ofSeconds(15);
        TimeWindows timeWindows = TimeWindows.ofSizeAndGrace(windowSize, graceWindowSize);
       // TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(windowSize);

        KTable<Windowed<String>, Long> orderCountPerStore = orderStream
                .map((key, value) -> KeyValue.pair(value.locationId(), value))
                .groupByKey(Grouped.with(Serdes.String(), new JsonSerde<>(Order.class)))
                .windowedBy(timeWindows)
                .count(Named.as(storeName), Materialized.as(storeName))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

        orderCountPerStore.toStream()
                .peek((key, value) -> {
                    log.info(" {} : tumblingWindow : key : {}, value : {}",storeName, key, value);
                    printLocalDateTimes(key, value);
                })
                .print(Printed.<Windowed<String>, Long>toSysOut().withLabel(storeName));

        ValueJoiner<Long, Store, TotalCountWithAddress> valueJoiner = TotalCountWithAddress::new;

        Joined<String, Long, Store> joinedParams =
                Joined.with(Serdes.String(), Serdes.Long(), new JsonSerde<>(Store.class));

        KStream<String,TotalCountWithAddress> totalCountWithStoreTable = orderCountPerStore
                .toStream()
                .map((key, value) -> KeyValue.pair(key.key(),value))
                .join(storesTable,valueJoiner,joinedParams);

        totalCountWithStoreTable.print(Printed.<String,TotalCountWithAddress>toSysOut().
                withLabel(storeName+"-by Store"));
    }

    private void aggregateOrdersByRevenue(KStream<String, Order> orderStream, String storeName, KTable<String, Store> storesTable) {

        Initializer<TotalRevenue> totalRevenueInitializer = TotalRevenue::new;

        Aggregator<String, Order, TotalRevenue> aggregator = (key, value, aggregate) -> aggregate.updateRunningRevenue(key, value);

        KTable<String, TotalRevenue> revenueTable = orderStream
                .map((key, value) -> KeyValue.pair(value.locationId(), value))
                .groupByKey(Grouped.with(Serdes.String(), new JsonSerde<>(Order.class)))
                .aggregate(totalRevenueInitializer,
                        aggregator,
                        Materialized.<String, TotalRevenue, KeyValueStore<Bytes, byte[]>>as(storeName)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(TotalRevenue.class)));

        revenueTable.toStream()
                .print(Printed.<String, TotalRevenue>toSysOut().withLabel(storeName));

        ValueJoiner<TotalRevenue, Store, TotalRevenueWithAddress> valueJoiner = TotalRevenueWithAddress::new;

        KTable<String,TotalRevenueWithAddress> revenueWithStoreTable = revenueTable.join(storesTable,valueJoiner);

        revenueWithStoreTable.toStream().print(Printed.<String,TotalRevenueWithAddress>toSysOut().
                withLabel(storeName+"-by Store"));

    }

    private void aggregateOrdersByRevenueByTimeWindows(KStream<String, Order> orderStream, String storeName, KTable<String, Store> storesTable)
    {
        Duration windowSize = Duration.ofSeconds(15);

        TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(windowSize);

        Initializer<TotalRevenue> totalRevenueInitializer = TotalRevenue::new;

        Aggregator<String, Order, TotalRevenue> aggregator = (key, value, aggregate) -> aggregate.updateRunningRevenue(key, value);

        KTable<Windowed<String>, TotalRevenue> revenueTable = orderStream
                .map((key, value) -> KeyValue.pair(value.locationId(), value))
                .groupByKey(Grouped.with(Serdes.String(), new JsonSerde<>(Order.class)))
                .windowedBy(timeWindows)
                .aggregate(totalRevenueInitializer,
                        aggregator,
                        Materialized.<String, TotalRevenue, WindowStore<Bytes, byte[]>>as(storeName)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(TotalRevenue.class)));

        revenueTable.toStream()
                .peek((key, value) -> {
                    log.info(" {} : tumblingWindow : key : {}, value : {}", storeName, key, value);
                    printLocalDateTimes(key,value);
                })
                .print(Printed.<Windowed<String>, TotalRevenue>toSysOut().withLabel( storeName));

    }

    private static void printLocalDateTimes(Windowed<String> key, Object value) {
        Instant startTime = key.window().startTime();
        Instant endTime = key.window().endTime();
        log.info("startTime : {} , endTime : {}, Count : {}", startTime, endTime, value);
        LocalDateTime startLDT = LocalDateTime.ofInstant(startTime, ZoneId.of(ZoneId.SHORT_IDS.get("IST")));
        LocalDateTime endLDT = LocalDateTime.ofInstant(endTime, ZoneId.of(ZoneId.SHORT_IDS.get("IST")));
        log.info("startLDT : {} , endLDT : {}, Count : {}", startLDT, endLDT, value);
    }

}
