package com.kafka.greetingstreams.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kafka.greetingstreams.domain.Greeting;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class GreetingSerdes implements Serde<Greeting> {

    /*
     Serde is class in kafka streams that handles serialization and deserialization
    our record contains latest timeStamp so serializing and deserializing properly we need to
    register module
     */
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    @Override
    public Serializer<Greeting> serializer() {
        return new GreetingSerializer(objectMapper);
    }

    @Override
    public Deserializer<Greeting> deserializer() {
        return new GreetingDeSerializer(objectMapper);
    }

}
