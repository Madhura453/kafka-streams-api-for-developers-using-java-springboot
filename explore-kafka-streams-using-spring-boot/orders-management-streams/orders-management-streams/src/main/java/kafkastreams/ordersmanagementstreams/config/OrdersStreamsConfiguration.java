package kafkastreams.ordersmanagementstreams.config;

import kafkastreams.ordersmanagementstreams.exceptionhandler.StreamsProcessorCustomErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.streams.RecoveringDeserializationExceptionHandler;

import java.util.Map;

@Configuration
@Slf4j
public class OrdersStreamsConfiguration {

    @Autowired
    KafkaProperties kafkaProperties;

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {

        Map<String, Object> properties = kafkaProperties.buildStreamsProperties();
        properties.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                RecoveringDeserializationExceptionHandler.class);
        properties.put(RecoveringDeserializationExceptionHandler.KSTREAM_DESERIALIZATION_RECOVERER, recoverer());
      // properties.put(StreamsConfig.STATE_DIR_CONFIG, String.format("%s%s", springApplicationName, serverPort));
        return new KafkaStreamsConfiguration(properties);
    }


    @Bean
    public StreamsBuilderFactoryBeanConfigurer streamsBuilderFactoryBeanConfigurer()
    {
        return factoryBean -> {
            factoryBean.setStreamsUncaughtExceptionHandler(new StreamsProcessorCustomErrorHandler());

        };

    }

    private ConsumerRecordRecoverer recoverer() {
        return (consumerRecord, exception) ->
        {
            log.error("Exception is : {} Failed Record : {} ", exception.getMessage(), consumerRecord);
        };

    }


}
