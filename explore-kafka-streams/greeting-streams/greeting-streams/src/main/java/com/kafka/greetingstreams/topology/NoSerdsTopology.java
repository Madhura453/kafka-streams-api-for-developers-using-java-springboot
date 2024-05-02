package com.kafka.greetingstreams.topology;

import com.kafka.greetingstreams.exploreOperators.ExploreOperators;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

public class NoSerdsTopology {

    private static String sourceTopic = "greetings";
    // @Value("${sink.topic}")
    private static String sinkTopic = "greetings_uppercase";

    public static Topology buildTopology() {

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, String> greetingsStream =
                streamsBuilder.stream(sourceTopic);

        greetingsStream.print(Printed.<String, String>toSysOut().withLabel("greetingsStream"));

        KStream<String, String> modifiedStream = ExploreOperators.peekOperator(greetingsStream);

        modifiedStream.print(Printed.<String, String>toSysOut().withLabel("modifiedStream"));

        modifiedStream.to(sinkTopic);
        return streamsBuilder.build();
    }
}
