package com.kafka.greetingsstreamsspringboot.topology;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GreetingsStreamsTopology {

    public static String sourceTopic = "greetings";
    public static String sinkTopic = "greetings_uppercase";

    @Autowired
    public void process(StreamsBuilder streamsBuilder) {
        KStream<String, String> greetingsStream =
                streamsBuilder.stream(sourceTopic, Consumed.with(Serdes.String(), Serdes.String()));
        greetingsStream.print(Printed.<String, String>toSysOut().withLabel("greetingsStream"));
        KStream<String, String> modifiedStream =
                greetingsStream
                        .mapValues((readOnlyKey, value) -> value.toUpperCase());
        modifiedStream.print(Printed.<String, String>toSysOut().withLabel("modifiedStream"));
        modifiedStream.to(sinkTopic, Produced.with(Serdes.String(), Serdes.String()));

    }
}
