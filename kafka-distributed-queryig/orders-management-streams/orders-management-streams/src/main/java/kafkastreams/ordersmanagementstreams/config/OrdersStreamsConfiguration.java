package kafkastreams.ordersmanagementstreams.config;

import kafkastreams.ordersmanagementstreams.exceptionhandler.StreamsProcessorCustomErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.streams.RecoveringDeserializationExceptionHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Configuration
@Slf4j
public class OrdersStreamsConfiguration {

    @Autowired
    KafkaProperties kafkaProperties;

    @Value("${server.port}")
    private Integer port;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() throws UnknownHostException {

        Map<String, Object>  streamProperties = kafkaProperties.buildStreamsProperties(null);
        streamProperties.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                RecoveringDeserializationExceptionHandler.class);
        streamProperties.put(RecoveringDeserializationExceptionHandler.KSTREAM_DESERIALIZATION_RECOVERER, recoverer());
        streamProperties.put(StreamsConfig.APPLICATION_SERVER_CONFIG, InetAddress.getLocalHost().getHostAddress()+":"+port);
        streamProperties.put(StreamsConfig.STATE_DIR_CONFIG,String.format("%s%s",applicationName,port));

        return new KafkaStreamsConfiguration(streamProperties);
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
