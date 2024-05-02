package com.kafka.orderskafkastreamsapp.launcher;

import com.kafka.orderskafkastreamsapp.topology.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

@Slf4j
public class OrdersKafkaStreamApp {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "orders-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // Topology topology = OrdersTopology.buildTopology();
        // Topology topology = AggregationInOrdersTopology.buildTopology();
        // Topology topology = SelectKeyForReKeyingTopology.buildTopology();

        Topology topology = JoinInOrdersTopology.buildTopology();

        KafkaStreams steams = new KafkaStreams(topology, properties);

        Runtime.getRuntime().addShutdownHook(new Thread(steams::close));
        try {
            steams.start();
        } catch (Exception e) {
            log.error("exception occurred during staring of the application");
        }
    }

}
