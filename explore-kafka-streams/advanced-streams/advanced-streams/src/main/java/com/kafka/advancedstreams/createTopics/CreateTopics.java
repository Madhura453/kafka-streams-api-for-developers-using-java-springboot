package com.kafka.advancedstreams.createTopics;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
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
    String AGGREGATE_TOPIC = "aggregate";

    String WINDOW_WORDS_TOPIC = "windows-words";

    public static String ALPHABETS_TOPIC = "alphabets";
    public static String ALPHABETS_ABBREVIATIONS_TOPIC = "alphabets_abbreviations";

    public void createTopics(Properties properties) {
        AdminClient adminClient = AdminClient.create(properties);
        List<String> topicsList = List.of(sourceTopic, sinkTopic, "greetingsIndia", WORDS_Topic,
                AGGREGATE_TOPIC, ALPHABETS_TOPIC, ALPHABETS_ABBREVIATIONS_TOPIC,
                WINDOW_WORDS_TOPIC);
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
