package com.kafka.greetingsstreamsspringboot.topology;

import com.kafka.greetingsstreamsspringboot.domain.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JsonSerdeTopology {

    public static String sourceTopic = "serde_greetings";
    public static String sinkTopic = "serde_greetings_uppercase";

    @Autowired
    public void process(StreamsBuilder streamsBuilder) {
        KStream<String, Greeting> greetingsStream =
                streamsBuilder.stream(sourceTopic,
                        Consumed.with(Serdes.String(),
                                new JsonSerde<>(Greeting.class)));
        greetingsStream.print(Printed.<String, Greeting>toSysOut().withLabel("greetingsStream"));
        KStream<String, Greeting> modifiedStream =
                greetingsStream
                        .mapValues((readOnlyKey, value) ->
                                new Greeting(value.message().toUpperCase(), value.timeStamp()));
        modifiedStream.print(Printed.<String, Greeting>toSysOut().withLabel("modifiedStream"));
        modifiedStream.to(sinkTopic, Produced.with(Serdes.String(), new JsonSerde<>(Greeting.class)));

    }
}

