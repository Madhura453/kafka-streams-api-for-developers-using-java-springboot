package com.kafka.greetingstreams.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
public class JsonDeSerializer<T> implements Deserializer<T> {

    private Class<T> destinationClass;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    /*
        In the constructor we got to know which type of class it is like is it
        Greeting class or Employee class.
        Through the constructor we are inject what the type of class it is
         */
    public JsonDeSerializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(data, destinationClass);
        } catch (IOException e) {
            log.error("IOException deserializer: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
