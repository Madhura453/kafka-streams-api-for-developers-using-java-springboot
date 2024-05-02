package com.kafka.greetingstreams.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kafka.greetingstreams.domain.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class JsonSerializer <T> implements Serializer<T> {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @Override
    public byte[] serialize(String topic, T genericType) {
        try {
            return objectMapper.writeValueAsBytes(genericType);
        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException: {}",e);
            throw new RuntimeException(e);
        }
    }


}
