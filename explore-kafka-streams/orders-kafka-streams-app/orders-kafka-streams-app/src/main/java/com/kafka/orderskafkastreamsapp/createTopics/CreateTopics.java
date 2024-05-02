package com.kafka.orderskafkastreamsapp.createTopics;

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

    private String OrdersSourceTopic = "orders";
    private static String STORES_TOPIC = "stores";

    public final String GENERAL_ORDERS_TOPIC = "general_orders";
    public final String RESTAURANT_ORDERS_TOPIC = "restaurant_orders";

    @Value("${partitions}")
    private Integer partitions;
    @Value("${replication}")
    private Short replication;

    public void createTopics(Properties properties) {
        AdminClient adminClient = AdminClient.create(properties);
        List<String> topicsList = List.of(OrdersSourceTopic, STORES_TOPIC, GENERAL_ORDERS_TOPIC, RESTAURANT_ORDERS_TOPIC);
        List<NewTopic> topicList = topicsList.stream().map(topic -> new NewTopic(topic, partitions, replication)).collect(Collectors.toList());
        CreateTopicsResult createTopicsResult = adminClient.createTopics(topicList);
        try {
            createTopicsResult.all().get();
            log.info("All topics created successfully");
        } catch (ExecutionException | InterruptedException e) {
            log.error("exception occurred while creating topics : {}", e.getMessage());
        }
    }

}
