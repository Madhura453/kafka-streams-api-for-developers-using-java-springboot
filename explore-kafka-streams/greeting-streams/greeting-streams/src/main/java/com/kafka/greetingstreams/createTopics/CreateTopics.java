package com.kafka.greetingstreams.createTopics;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CreateTopics {

    @Value("${source.topic}")
    private String sourceTopic;
    @Value("${sink.topic}")
    private String sinkTopic;
    @Value("${partitions}")
    private Integer partitions;
    @Value("${replication}")
    private Short replication;
    public String WORDS_Topic = "words";
    public void createTopics(Properties properties) {
        AdminClient adminClient = AdminClient.create(properties);
        List<String> topicsList = List.of(sourceTopic, sinkTopic, "greetingsIndia",WORDS_Topic);
        List<NewTopic> topicList = topicsList.stream().
                map(topic -> new NewTopic(topic, partitions, replication))
                .collect(Collectors.toList());
        CreateTopicsResult createTopicsResult = adminClient.createTopics(topicList);
        try {
            createTopicsResult.all().get();
            log.info("All topics created successfully");
        } catch (ExecutionException | InterruptedException e) {
            log.error("exception occurred while creating topics : {}", e.getMessage());
        }
    }

}
