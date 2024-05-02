package com.kafka.greetingstreams.launcher;

import com.kafka.greetingstreams.topology.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;

import java.util.Properties;

@Slf4j
public class GreetingsStreamApp {

    public static void main(String[] args) {
        Properties properties = new Properties();
        // APPLICATION_ID_CONFIG is like consumer group id of kafka consumers. like bookmark where to stat
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "grretins-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // means reads the latest records from the topic
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        /* Ideal value for StreamsConfig.NUM_STREAM_THREADS_CONFIG
        =Runtime.getRuntime().availableProcessors();
         */
        properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "2");
        // properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG,availableProcessors);
        /*override default kafka default exception handler LogAndFailExceptionHandler
            to LogAndContinueExceptionHandler.*/
        properties.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                LogAndContinueExceptionHandler.class);


        //  Topology topology= GreetingsTopology.buildTopology();
        // Topology mergedTopology = MergeOperatorTopology.buildTopology();
        //  Topology topology= CustomSerdTopology.buildTopology();


        // Topology topology = TopologyUsingGenericSerializer.buildTopology();
        Topology topology = ExceptionHandlingInTopology.buildTopology();

        // This kafkaStreams will execute the topology

        KafkaStreams steams = new KafkaStreams(topology, properties);
        // which topology we want to start put that topology name
        /*
        Anytime you shut down the app. The ShutdownHook releases all the resources
        that used by greeting app topology
         */
        Runtime.getRuntime().addShutdownHook(new Thread(steams::close));
        try {
            steams.start();
        } catch (Exception e) {
            log.error("exception occurred during staring of the application");
        }
    }


}
