package com.kafka.greetingstreams;

import com.kafka.greetingstreams.topology.GreetingsTopology;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
@Slf4j
public class GreetingStreamsApplication implements CommandLineRunner {

	@Autowired
	GreetingsTopology greetingsTopology;

	public static void main(String[] args) {
		SpringApplication.run(GreetingStreamsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
