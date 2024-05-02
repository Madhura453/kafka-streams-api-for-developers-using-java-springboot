package kafkastreams.ordersmanagementstreams.config;

import kafkastreams.ordersmanagementstreams.topology.OrdersTopology;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
public class CreateTopics {

    @Bean
    public NewTopic topicBuilder() {
        return TopicBuilder.name(OrdersTopology.ORDERS_TOPIC)
                .partitions(2)
                .replicas(1)
                .build();

    }

    @Bean
    public NewTopic storeTopicBuilder() {
        return TopicBuilder.name(OrdersTopology.STORES_TOPIC)
                .partitions(2)
                .replicas(1)
                .build();

    }

}
