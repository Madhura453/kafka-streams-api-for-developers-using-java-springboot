package com.kafka.greetingstreams.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.greetingstreams.domain.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
public class GreetingDeSerializer implements Deserializer<Greeting> {

    private ObjectMapper objectMapper;

    public GreetingDeSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Greeting deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data,Greeting.class);
        } catch (IOException e) {
            log.error("IOException: {}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }
}
