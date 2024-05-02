package com.kafka.greetingsstreamsspringboot.config;

import com.kafka.greetingsstreamsspringboot.topology.GreetingsStreamsTopology;
import com.kafka.greetingsstreamsspringboot.topology.JsonSerdeTopology;
import com.kafka.greetingsstreamsspringboot.topology.UncaughtExceptionsInTopology;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class GreetingsStreamsConfiguration {

    @Bean
    public NewTopic greetingsTopic()
    {
        return TopicBuilder
                .name(GreetingsStreamsTopology.sourceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic greetingsSinkTopic()
    {
        return TopicBuilder
                .name(GreetingsStreamsTopology.sinkTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic serdeGreetingsTopic()
    {
        return TopicBuilder
                .name(JsonSerdeTopology.sourceTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic serdeGreetingsSinkTopic()
    {
        return TopicBuilder
                .name(JsonSerdeTopology.sinkTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ExceptionGreetingsSinkTopic()
    {
        return TopicBuilder
                .name(UncaughtExceptionsInTopology.sinkTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
