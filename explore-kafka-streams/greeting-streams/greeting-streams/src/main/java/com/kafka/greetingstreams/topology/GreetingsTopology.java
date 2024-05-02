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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GreetingsTopology {

    // @Value("${source.topic}")
    private static String sourceTopic = "greetings";
    // @Value("${sink.topic}")
    private static String sinkTopic = "greetings_uppercase";

    public static Topology buildTopology() {
        // By using streams builder we can build source processor, stream processor, sink processor
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        // read from the sourceTopic with key is string value is string
        // it was inside uses consumer API.
        // // It is source processor
        KStream<String, String> greetingsStream = streamsBuilder.stream(sourceTopic, Consumed.with(Serdes.String(), Serdes.String()));

        // prints the greetingsStream

        greetingsStream.print(Printed.<String, String>toSysOut().withLabel("greetingsStream"));

        // it is stream processor.
        // inside ExploreOperators there are different operations. so called accordingly
       // KStream<String, String> modifiedStream = ExploreOperators.filterOperator(greetingsStream);
       // KStream<String, String> modifiedStream = ExploreOperators.filterNotOperator(greetingsStream);
      //  KStream<String, String> modifiedStream = ExploreOperators.mapOperator(greetingsStream);
      //  KStream<String, String> modifiedStream = ExploreOperators.flatmapOperator(greetingsStream);
      //  KStream<String, String> modifiedStream = ExploreOperators.flatmapValueOperator(greetingsStream);
        KStream<String, String> modifiedStream = ExploreOperators.peekOperator(greetingsStream);
        /*       KStream<String, String> modifiedStream    =greetingsStream
               .filter((readOnlyKey, value) -> value.length() > 3)
                .mapValues((readOnlyKey, value) -> value.toUpperCase());
 */
        //prints modified stream
        modifiedStream.print(Printed.<String, String>toSysOut().withLabel("modifiedStream"));

//         It will automatically publish message to the topic, produce key is string value is string
//         it was inside uses producer API
//         it is consumer processor
//
        modifiedStream.to(sinkTopic, Produced.with(Serdes.String(), Serdes.String()));
        return streamsBuilder.build();
    }
}
