package com.kafka.greetingstreams.launcher;

import com.kafka.greetingstreams.createTopics.CreateTopics;
import com.kafka.greetingstreams.topology.GreetingsTopology;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping("/topo")
@Slf4j
public class CreateTopicsController {

    @Autowired
    private CreateTopics createTopics;
    @Autowired
    GreetingsTopology greetingsTopology;
    @GetMapping("/uppercase")
    public ResponseEntity<String> executeTopology()
    {
        Properties properties = new Properties();
        // APPLICATION_ID_CONFIG is like consumer group id of kafka consumers. like bookmark where to stat
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "grretins-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // means reads the latest records from the topic
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        createTopics.createTopics(properties);
        return ResponseEntity.ok("Function executed successfully");
    }




}
