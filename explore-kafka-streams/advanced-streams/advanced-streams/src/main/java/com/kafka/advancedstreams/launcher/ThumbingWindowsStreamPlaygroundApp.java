package com.kafka.advancedstreams.launcher;

import com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreTumblingWindowTopology;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;


@Slf4j
public class ThumbingWindowsStreamPlaygroundApp {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "windows");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        /*
         the commit interval we have set as ten seconds.
        The reason why I say ten seconds is that this result is printed
        Every 10 seconds the result will print
         */
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,"10000");

        Topology topology = ExploreTumblingWindowTopology.buildTopology();

        KafkaStreams steams = new KafkaStreams(topology, properties);

        Runtime.getRuntime().addShutdownHook(new Thread(steams::close));
        try {
            steams.start();
        } catch (Exception e) {
            log.error("exception occurred during staring of the application");
        }
    }
}
