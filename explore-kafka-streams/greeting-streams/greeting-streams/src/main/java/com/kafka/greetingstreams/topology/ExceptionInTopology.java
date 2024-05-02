package com.kafka.greetingstreams.topology;

import com.kafka.greetingstreams.domain.Greeting;
import com.kafka.greetingstreams.serdes.SerdesFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

@Slf4j
public class ExceptionInTopology {

    private static String sourceTopic = "greetings";
    // @Value("${sink.topic}")
    private static String sinkTopic = "greetings_uppercase";

    private static String mergeTopic = "greetingsIndia";

    public static Topology buildTopology() {

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, Greeting> greetingsStream =
                streamsBuilder.stream(sourceTopic, Consumed.with(Serdes.String(), SerdesFactory.greetingSerdeUsingGenerics()));

        KStream<String, Greeting> modifiedStream = exploreErrors(greetingsStream);

        modifiedStream.to(sinkTopic, Produced.with(Serdes.String(), SerdesFactory.greetingSerdeUsingGenerics()));

        return streamsBuilder.build();
    }

    /*
    if exception occurred inside topology
    The exception message is "Streams client stopped to ERROR completely"
     */
    public static KStream<String,Greeting> exploreErrors(KStream<String, Greeting> greetingsStream)
    {
        return greetingsStream.mapValues(((readOnlyKey, value) ->
        {
            if(value.message().equals("Transient Error"))
            {
                throw new IllegalStateException(value.message());
            }
            return new Greeting(value.message().toUpperCase(),value.timeStamp());
        }));
    }

}
