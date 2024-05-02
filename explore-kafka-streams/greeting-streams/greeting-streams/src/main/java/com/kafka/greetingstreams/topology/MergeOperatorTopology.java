package com.kafka.greetingstreams.topology;

import com.kafka.greetingstreams.exploreOperators.ExploreOperators;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MergeOperatorTopology {
    private static String sourceTopic = "greetings";
    // @Value("${sink.topic}")
    private static String sinkTopic = "greetings_uppercase";

    private static String mergeTopic = "greetingsIndia";

    public static Topology buildTopology() {

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        /* Consumed.with(Serdes.String(), Serdes.String())
        here key deserializer and value deserializer
        */
        KStream<String, String> greetingsStream =
                streamsBuilder.stream(sourceTopic, Consumed.with(Serdes.String(), Serdes.String()));

        KStream<String, String> greetingsIndiaStream =
                streamsBuilder.stream(mergeTopic, Consumed.with(Serdes.String(), Serdes.String()));

        KStream<String, String> mergedStream = greetingsStream.merge(greetingsIndiaStream);

        mergedStream.print(Printed.<String, String>toSysOut().withLabel("mergedStream"));

        KStream<String, String> modifiedStream = mergedStream.mapValues((readOnlyKey,value)->value.toUpperCase());

        modifiedStream.print(Printed.<String, String>toSysOut().withLabel("modifiedStream"));

        modifiedStream.to(sinkTopic, Produced.with(Serdes.String(), Serdes.String()));
        /*
        Produced.with(Serdes.String(), Serdes.String())
         here key serializer and value serializer
         */
        return streamsBuilder.build();
    }
}
