package com.kafka.orderskafkastreamsapp.launcher;

import com.kafka.orderskafkastreamsapp.topology.JoinInOrdersTopology;
import com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology;
import com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology;
import com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

@Slf4j
public class WindowOrdersKafkaStreamApp {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "orders-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        log.info("giving custom timestamp extractor in place of OrderTimeStampExtractor");
        properties.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, OrderTimeStampExtractor.class);

        Topology topology = AggregateWithWindowsTopology.buildTopology();

        KafkaStreams steams = new KafkaStreams(topology, properties);

        Runtime.getRuntime().addShutdownHook(new Thread(steams::close));
        try {
            steams.start();
        } catch (Exception e) {
            log.error("exception occurred during staring of the application");
        }
    }

}
