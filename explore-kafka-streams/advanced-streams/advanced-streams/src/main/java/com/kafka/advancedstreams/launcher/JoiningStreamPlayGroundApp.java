package com.kafka.advancedstreams.launcher;

import com.kafka.advancedstreams.topology.ExploreAggregateOperatorsTopology;
import com.kafka.advancedstreams.topology.ExploreJoinsOperatorsTopology;
import com.kafka.advancedstreams.topology.JoinKStreamWithKStreamTopology;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;

@Slf4j
public class JoiningStreamPlayGroundApp {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "joins");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        //Topology topology = ExploreJoinsOperatorsTopology.buildTopology();

       Topology topology = JoinKStreamWithKStreamTopology.buildTopology();

        KafkaStreams steams = new KafkaStreams(topology, properties);

        Runtime.getRuntime().addShutdownHook(new Thread(steams::close));
        try {
            steams.start();
        } catch (Exception e) {
            log.error("exception occurred during staring of the application");
        }
    }
}
