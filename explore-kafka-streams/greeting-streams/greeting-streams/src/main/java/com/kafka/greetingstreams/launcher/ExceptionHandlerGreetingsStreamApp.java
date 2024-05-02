package com.kafka.greetingstreams.launcher;

import com.kafka.greetingstreams.exceptionhandler.StreamsDeserializationErrorHandler;
import com.kafka.greetingstreams.exceptionhandler.StreamsProcessorCustomErrorHandler;
import com.kafka.greetingstreams.exceptionhandler.StreamsSerializationExceptionHandler;
import com.kafka.greetingstreams.topology.ExceptionInTopology;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

@Slf4j
public class ExceptionHandlerGreetingsStreamApp {

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "grretins-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "2");
        log.info(" overriding default DESERIALIZATION_EXCEPTION_HANDLER to custom");
        properties.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, StreamsDeserializationErrorHandler.class);
        log.info("overriding DEFAULT_PRODUCTION_EXCEPTION_HANDLER to custom");
        properties.put(StreamsConfig.DEFAULT_PRODUCTION_EXCEPTION_HANDLER_CLASS_CONFIG,
                StreamsSerializationExceptionHandler.class);

        //Topology topology = TopologyUsingGenericSerializer.buildTopology();

        Topology topology = ExceptionInTopology.buildTopology();

        KafkaStreams steams = new KafkaStreams(topology, properties);
        log.info("To handle if any exception occurred in topology(stream processing logic)." +
                " we are setting custom StreamsProcessorCustomErrorHandler");
        steams.setUncaughtExceptionHandler(new StreamsProcessorCustomErrorHandler());

        Runtime.getRuntime().addShutdownHook(new Thread(steams::close));
        try {
            steams.start();
        } catch (Exception e) {
            log.error("exception occurred during staring of the application");
        }
    }
}
