package com.kafka.greetingstreams.topology;

import com.kafka.greetingstreams.domain.Greeting;
import com.kafka.greetingstreams.serdes.SerdesFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

public class CustomSerdTopology {

    private static String sourceTopic = "greetings";
    // @Value("${sink.topic}")
    private static String sinkTopic = "greetings_uppercase";

    private static String mergeTopic = "greetingsIndia";

    public static Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, Greeting> mergedStream = getCustomGreetingKStream(streamsBuilder);

        mergedStream.print(Printed.<String, Greeting>toSysOut().withLabel("mergedStream custom serde"));

        KStream<String, Greeting> modifiedStream =
                mergedStream.mapValues((key, value) -> new Greeting(value.message().toUpperCase(), value.timeStamp()));

        modifiedStream.print(Printed.<String, Greeting>toSysOut().withLabel("modifiedStream custom serde"));

        modifiedStream.to(sinkTopic, Produced.with(Serdes.String(), SerdesFactory.greetingSerde()));
        return streamsBuilder.build();
    }

    private static KStream<String, Greeting> getCustomGreetingKStream(StreamsBuilder streamsBuilder) {
        KStream<String, Greeting> greetingsStream =
                streamsBuilder.stream(sourceTopic, Consumed.with(Serdes.String(), SerdesFactory.greetingSerde()));

        KStream<String, Greeting> greetingsIndiaStream =
                streamsBuilder.stream(mergeTopic, Consumed.with(Serdes.String(), SerdesFactory.greetingSerde()));

        return greetingsStream.merge(greetingsIndiaStream);
    }

}
/*
madh-{"message":"applle","timeStamp":"2024-01-12T04:21:15.465858950"}=publishing format

 */