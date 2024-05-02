package com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts;

import com.kafka.orderskafkastreamsapp.domain.*;
import com.kafka.orderskafkastreamsapp.serdes.SerdesFactory;
import com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;
import org.slf4j.event.KeyValuePair;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
public class JoinOnWindowedAggregatedTopology {

    private static String ordersSourceTopic = "orders";

    private static String STORES_TOPIC = "stores";
    public static final String GENERAL_ORDERS_COUNT_WINDOWS_STORE = "general_orders_count_window";
    public static final String GENERAL_ORDERS_REVENUE_WINDOWS_STORE = "general_orders_revenue_window";

    public static final String RESTAURANT_ORDERS_COUNT_WINDOWS_STORE = "restaurant_orders_count_window";
    public static final String RESTAURANT_ORDERS_REVENUE_WINDOWS_STORE = "restaurant_orders_revenue_window";

    Predicate<String, Order> generalPredicate = (key, order) -> order.orderType().equals(OrderType.GENERAL);
    Predicate<String, Order> restaruntPredicate = (key, order) -> order.orderType().equals(OrderType.RESTAURANT);

    public static Topology buildTopology() {


        Predicate<String, Order> generalPredicate = (key, order) -> order.orderType().equals(OrderType.GENERAL);

        Predicate<String, Order> restaruntPredicate = (key, order) -> order.orderType().equals(OrderType.RESTAURANT);

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Order> orderStreams = streamsBuilder.stream(ordersSourceTopic,
                Consumed.with(Serdes.String(), SerdesFactory.orderSerdes())
                        .withTimestampExtractor(new OrderTimeStampExtractor()));
        /*
        the timestamp extractor specific to the stream instance
         */
        orderStreams
                .print(Printed.<String, Order>toSysOut().withLabel("orders"));


        KTable<String, Store> storesTable =
                streamsBuilder.table(STORES_TOPIC, Consumed.with(Serdes.String(), SerdesFactory.storeSerdes()));
        storesTable
                .toStream()
                .print(Printed.<String, Store>toSysOut().withLabel("stores_table"));

        orderStreams.split(Named.as("General_restaurant_stream")) //// General_restaurant_stream was any name we can give
                .branch(generalPredicate, Branched.withConsumer(generalOrderStream -> {
                   // joinAggregateOrdersByCountAndStoreByTimeWindows(generalOrderStream, GENERAL_ORDERS_COUNT_WINDOWS_STORE, storesTable);
                    joinAggregateOrdersByRevenueAndStoreByTimeWindows(generalOrderStream, GENERAL_ORDERS_REVENUE_WINDOWS_STORE, storesTable);

                }))
                .branch(restaruntPredicate, Branched.withConsumer(restaurantOrderStream ->
                {
                    //joinAggregateOrdersByCountAndStoreByTimeWindows(restaurantOrderStream, RESTAURANT_ORDERS_COUNT_WINDOWS_STORE, storesTable);
                    joinAggregateOrdersByRevenueAndStoreByTimeWindows(restaurantOrderStream, RESTAURANT_ORDERS_REVENUE_WINDOWS_STORE, storesTable);
                }));
        return streamsBuilder.build();
    }

    private static void joinAggregateOrdersByCountAndStoreByTimeWindows(KStream<String, Order> orderStream, String joinWindowCountStoreName,
                                                          KTable<String, Store> storesTable) {

        Duration windowSize = Duration.ofSeconds(15);
        TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(windowSize);

        KTable<Windowed<String>, Long> orderCountPerStore = orderStream
                .map((key, value) -> KeyValue.pair(value.locationId(), value))
                .groupByKey(Grouped.with(Serdes.String(), SerdesFactory.orderSerdes()))
                .windowedBy(timeWindows)
                .count(Named.as(joinWindowCountStoreName), Materialized.as(joinWindowCountStoreName))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

        orderCountPerStore.toStream()
                .peek((key, value) -> {
                    log.info(" {} : tumblingWindow : key : {}, value : {}",joinWindowCountStoreName, key, value);
                    printLocalDateTimes(key, value);
                })
                .print(Printed.<Windowed<String>, Long>toSysOut().withLabel(joinWindowCountStoreName));

        ValueJoiner<Long, Store, TotalCountWithAddress> valueJoiner = TotalCountWithAddress::new;

        Joined<String, Long, Store> joinedParams =
                Joined.with(Serdes.String(), Serdes.Long(), SerdesFactory.storeSerdes());

        KStream<String,TotalCountWithAddress> totalCountWithStoreTable = orderCountPerStore
                .toStream()
                .map((key, value) -> KeyValue.pair(key.key(),value))
                .join(storesTable,valueJoiner,joinedParams);

        totalCountWithStoreTable.print(Printed.<String,TotalCountWithAddress>toSysOut().
                withLabel(joinWindowCountStoreName+"-by Store"));

    }

    private static void joinAggregateOrdersByRevenueAndStoreByTimeWindows(KStream<String, Order> orderStream, String joinAggregatorWindowStoreName,
                                                              KTable<String, Store> storesTable) {
        Duration windowSize = Duration.ofSeconds(15);

        TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(windowSize);

        Initializer<TotalRevenue> totalRevenueInitializer = TotalRevenue::new;

        Aggregator<String, Order, TotalRevenue> aggregator = (key, value, aggregate) -> aggregate.updateRunningRevenue(key, value);

        KTable<Windowed<String>, TotalRevenue> revenueTable = orderStream
                .map((key, value) -> KeyValue.pair(value.locationId(), value))
                .groupByKey(Grouped.with(Serdes.String(), SerdesFactory.orderSerdes()))
                .windowedBy(timeWindows)
                .aggregate(totalRevenueInitializer,
                        aggregator,
                        Materialized.<String, TotalRevenue, WindowStore<Bytes, byte[]>>as(joinAggregatorWindowStoreName)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(SerdesFactory.totalRevenueSerdes()));
        revenueTable.toStream()
                .peek((key, value) -> {
                    log.info(" {} : tumblingWindow : key : {}, value : {}", joinAggregatorWindowStoreName, key, value);
                    printLocalDateTimes(key, value);
                })
                .print(Printed.<Windowed<String>, TotalRevenue>toSysOut().withLabel(joinAggregatorWindowStoreName));

        ValueJoiner<TotalRevenue, Store, TotalRevenueWithAddress> valueJoiner = TotalRevenueWithAddress::new;

        /*
        It is always recommended use joinParams
        key is string and first one value is TotalRevenue and second one value store
         */
        Joined<String, TotalRevenue, Store> joinedParams =
                Joined.with(Serdes.String(), SerdesFactory.totalRevenueSerdes(), SerdesFactory.storeSerdes());

        KStream<String, TotalRevenueWithAddress> revenueWithStoreTable =
                revenueTable
                        .toStream()
                        .map((key, value) -> KeyValue.pair(key.key(), value))
                        .join(storesTable, valueJoiner,joinedParams);

        revenueWithStoreTable
                .print(Printed.<String, TotalRevenueWithAddress>toSysOut().
                        withLabel(joinAggregatorWindowStoreName + "-by Store"));
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
