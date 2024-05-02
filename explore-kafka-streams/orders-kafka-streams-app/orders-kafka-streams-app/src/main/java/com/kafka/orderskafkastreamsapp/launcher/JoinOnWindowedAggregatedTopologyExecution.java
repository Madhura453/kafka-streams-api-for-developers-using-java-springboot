package com.kafka.orderskafkastreamsapp.launcher;

import com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology;
import com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

@Slf4j
public class JoinOnWindowedAggregatedTopologyExecution {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "orders-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        log.info("giving custom timestamp extractor in place of OrderTimeStampExtractor");

       // properties.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, OrderTimeStampExtractor.class);
        /*
        commenting because in joins we need to produce both store and orders information.
        But OrderTimeStampExtractor.class is specific to orders
        if you produce stores we will get error.
        So if you have use cases where we are streaming from multiple Kafka topics, setting up a common timestamp
        extractor(OrderTimeStampExtractor.class) is going to be a problem.
        In the topology, we can set up the timestamp extractor specific to each
       instance of the stream
         */

        Topology topology = JoinOnWindowedAggregatedTopology.buildTopology();


        KafkaStreams steams = new KafkaStreams(topology, properties);

        Runtime.getRuntime().addShutdownHook(new Thread(steams::close));
        try {
            steams.start();
        } catch (Exception e) {
            log.error("exception occurred during staring of the application");
        }
    }
}
