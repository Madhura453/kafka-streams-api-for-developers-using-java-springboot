package kafkastreams.ordersmanagementstreams;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.apache.kafka.streams.KafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class OrdersManagementStreamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersManagementStreamsApplication.class, args);
	}

}
