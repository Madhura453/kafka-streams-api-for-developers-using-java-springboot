package com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts;

import com.kafka.orderskafkastreamsapp.domain.*;
import com.kafka.orderskafkastreamsapp.serdes.SerdesFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
public class AggregateWithWindowsTopology {

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
                   // aggregateOrdersCountByTimeWindows(generalOrderStream, GENERAL_ORDERS_COUNT_WINDOWS_STORE, storesTable);
                    aggregateOrdersByRevenueByTimeWindows(generalOrderStream, GENERAL_ORDERS_REVENUE_WINDOWS_STORE, storesTable);

                }))
                .branch(restaruntPredicate, Branched.withConsumer(restaurantOrderStream ->
                {
                    log.info("count the restaurant orders based on location id");
                   // aggregateOrdersCountByTimeWindows(restaurantOrderStream, RESTAURANT_ORDERS_COUNT_WINDOWS_STORE, storesTable);
                    aggregateOrdersByRevenueByTimeWindows(restaurantOrderStream, RESTAURANT_ORDERS_REVENUE_WINDOWS_STORE, storesTable);
                }));
        return streamsBuilder.build();
    }

    private static void aggregateOrdersCountByTimeWindows(KStream<String, Order> orderStream, String windowCountStoreName,
                                                          KTable<String, Store> storesTable) {

      //  log.info("count the orders with in time window size 15. This is thumbing window.");

        Duration windowSize = Duration.ofSeconds(15);
        TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(windowSize);
     /*
      windowedBy = before count. time window
      suppress= after count because its need to emitted to downstream
      want to make sure any time the defined windows are exhausted, then we want to have the result sent
      downstream. Here downstream is we just printing the result
         */
        KTable<Windowed<String>, Long> orderCount = orderStream
                .map((key, value) -> KeyValue.pair(value.locationId(), value))
                .groupByKey(Grouped.with(Serdes.String(), SerdesFactory.orderSerdes()))
                .windowedBy(timeWindows)
                .count(Named.as( windowCountStoreName), Materialized.as( windowCountStoreName))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

        orderCount.toStream()
                .peek((key, value) -> {
                    log.info(" {} : tumblingWindow : key : {}, value : {}", windowCountStoreName, key, value);
                    printLocalDateTimes(key,value);
                })
                .print(Printed.<Windowed<String>, Long>toSysOut().withLabel( windowCountStoreName));

    }

    private static void aggregateOrdersByRevenueByTimeWindows(KStream<String, Order> orderStream, String aggregatorWindowStoreName,
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
                        Materialized.<String, TotalRevenue, WindowStore<Bytes, byte[]>>as(aggregatorWindowStoreName)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(SerdesFactory.totalRevenueSerdes()));
/*
we are going to have the window data,So in that case we need to change this one to window store
because this is going to be storing the data as window string.
 */
        revenueTable.toStream()
                .peek((key, value) -> {
                    log.info(" {} : tumblingWindow : key : {}, value : {}", aggregatorWindowStoreName, key, value);
                    printLocalDateTimes(key,value);
                })
                .print(Printed.<Windowed<String>, TotalRevenue>toSysOut().withLabel( aggregatorWindowStoreName));

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
