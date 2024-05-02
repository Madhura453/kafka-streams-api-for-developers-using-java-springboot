package com.kafka.greetingstreams.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.greetingstreams.domain.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
@Slf4j
public class GreetingSerializer implements Serializer<Greeting> {
    private ObjectMapper objectMapper;

    public GreetingSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(String topic, Greeting greeting) {
        try {
            return objectMapper.writeValueAsBytes(greeting);
        } catch (JsonProcessingException e) {
           log.info("JsonProcessingException: {}",e);
           throw new RuntimeException(e);
        }
    }
}
