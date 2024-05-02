package com.kafka.orderskafkastreamsapp.topology;

import com.kafka.orderskafkastreamsapp.domain.Order;
import com.kafka.orderskafkastreamsapp.domain.OrderType;
import com.kafka.orderskafkastreamsapp.domain.Revenue;
import com.kafka.orderskafkastreamsapp.serdes.SerdesFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

@Slf4j
public class OrdersTopology {
    private static String ordersSourceTopic = "orders";
    public static final String GENERAL_ORDERS_TOPIC = "general_orders";

    public static final String RESTAURANT_ORDERS_TOPIC = "restaurant_orders";


    Predicate<String, Order> generalPredicate = (key, order) -> order.orderType().equals(OrderType.GENERAL);
    Predicate<String, Order> restaruntPredicate = (key, order) -> order.orderType().equals(OrderType.RESTAURANT);

    public static Topology buildTopology() {

        Predicate<String, Order> generalPredicate = (key, order) -> order.orderType().equals(OrderType.GENERAL);

        Predicate<String, Order> restaruntPredicate = (key, order) -> order.orderType().equals(OrderType.RESTAURANT);

        ValueMapper<Order, Revenue> revenueMapper = order -> new Revenue(order.locationId(), order.finalAmount());

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        // key is order id
        KStream<String, Order> orderStreams = streamsBuilder.stream(ordersSourceTopic,
                Consumed.with(Serdes.String(), SerdesFactory.orderSerdes()));
        orderStreams
                .print(Printed.<String, Order>toSysOut().withLabel("orders"));

        // split the orders based on order type
        /*
        created 2 branches out of original stream. original stream combination of both general and restaurant orders. by using
        split and branch used to split original stream into 2 different streams. applied predicate to each branch. based on
        predicate orders gets split into general orders and restaurant orders
        splits 2 branched  BranchedKStream in BranchedKStream we have our generalOrderStream records.
        split the orders and  convert to revenue records by revenueMapper
        and publish message to 2 different topics GENERAL_ORDERS_TOPIC, RESTAURANT_ORDERS_TOPIC
         */
        orderStreams.split(Named.as("General_restaurant_stream")) //// General_restaurant_stream was any name we can give
                .branch(generalPredicate, Branched.withConsumer(generalOrderStream -> {
                    generalOrderStream.print(Printed.<String, Order>toSysOut().withLabel("generalOrderStream"));
                    generalOrderStream.mapValues((key, value) -> revenueMapper.apply(value))
                            .to(GENERAL_ORDERS_TOPIC, Produced.with(Serdes.String(), SerdesFactory.revenueSerdes()));
                }))
                .branch(restaruntPredicate, Branched.withConsumer(restaurantOrderStream ->
                {
                    restaurantOrderStream.print(Printed.<String, Order>toSysOut().withLabel("restaurantOrderStream"));
                    restaurantOrderStream.mapValues((key, value) -> revenueMapper.apply(value))
                            .to(RESTAURANT_ORDERS_TOPIC, Produced.with(Serdes.String(), SerdesFactory.revenueSerdes()));
                }));
        return streamsBuilder.build();
    }
}
