package com.kafka.greetingsstreamsspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class GreetingsStreamsSpringbokApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreetingsStreamsSpringbokApplication.class, args);
	}

}
