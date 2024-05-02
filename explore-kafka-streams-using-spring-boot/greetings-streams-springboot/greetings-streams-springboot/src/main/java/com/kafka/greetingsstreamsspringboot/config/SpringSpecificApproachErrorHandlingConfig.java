package com.kafka.greetingsstreamsspringboot.config;

import com.kafka.greetingsstreamsspringboot.exceptionhandler.StreamsProcessorCustomErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SpringSpecificApproachErrorHandlingConfig {

    @Autowired
    KafkaProperties kafkaProperties;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {

        Map<String, Object> properties = kafkaProperties.buildStreamsProperties();
        /*
        kafkaProperties=copied from application.yml. go more into  KafkaProperties
        class there @ConfigurationProperties(prefix = "spring.kafka")
         */

        properties.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                RecoveringDeserializationExceptionHandler.class);
        properties.put(RecoveringDeserializationExceptionHandler.KSTREAM_DESERIALIZATION_RECOVERER, recoverer());
        return new KafkaStreamsConfiguration(properties);
    }


    @Bean
    public StreamsBuilderFactoryBeanConfigurer streamsBuilderFactoryBeanConfigurer() {
        log.info("Inside streamsBuilderFactoryBeanConfigurer");
        /*
        To handle exception in topology
         */
        return factoryBean -> {
            factoryBean.setStreamsUncaughtExceptionHandler(new StreamsProcessorCustomErrorHandler());

            /*
            factoryBean.set
            his factoryBean configure to do a lot of different configurations
            if you pass a set value, you have access to many different setter methods
            Basically you get to override from the default configurations
             */
        };

    }

    private ConsumerRecordRecoverer recoverer() {
        /*
        Any time Deserialization exception comes we are logging
         */
        return (consumerRecord, exception) ->
        {
            log.error("Exception is : {} Failed Record : {} ", exception.getMessage(), consumerRecord);
        };

        /*
        RecoveringDeserializationExceptionHandler it holds ConsumerRecordRecoverer recoverer

         public RecoveringDeserializationExceptionHandler(ConsumerRecordRecoverer recoverer)

         ConsumerRecordRecoverer extends BiConsumer<ConsumerRecord<?, ?>, Exception>

          ConsumerRecordRecoverer is some  BiConsumer<ConsumerRecord<
         */
    }


}
