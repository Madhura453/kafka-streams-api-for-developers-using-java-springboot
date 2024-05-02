package com.kafka.greetingstreams.topology;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

@Slf4j
public class ExploreKTableTopology {
    public static String WORDS_Topic = "words";

    public static Topology buildTopologyWithNoMaterializedStore() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KTable<String, String> wordsTable =
                streamsBuilder.table(WORDS_Topic, Consumed.with(Serdes.String(), Serdes.String()));
        log.info("changing kTable to KStream.Because we can't print logs in kTable");
        KStream<String, String> wordsTableStream = wordsTable.filter((key, value) ->
                value.length() > 2).toStream();
        wordsTableStream.print(Printed.<String, String>toSysOut().withLabel("wordsTableStream"));
        return streamsBuilder.build();
        /*
        The ouput was

        [wordsTableStream]: A, Apple
       [wordsTableStream]: A, Alligator
       [wordsTableStream]: A, Ambulance
       [wordsTableStream]: B, Bus
       [wordsTableStream]: B, Baby
       without Materialized.as( the kTable doesn't print latest key and value
         */
    }

    public static Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KTable<String, String> wordsTable =
                streamsBuilder.table(WORDS_Topic, Consumed.with(Serdes.String(), Serdes.String()),
                        Materialized.as("words-store"));
        log.info("changing kTable to KStream.Because we can't print logs in kTable");
        KStream<String, String> wordsTableStream =
                wordsTable.filter((key, value) -> value.length() > 2)
                        .mapValues((key, value) -> value.toUpperCase()).toStream();
        wordsTableStream
                .peek((key, value) -> log.info("key: {}, value: {}", key, value))
                .print(Printed.<String, String>toSysOut().withLabel("wordsTableStream"));
        return streamsBuilder.build();
 /*
        Materialized.as = to get latest record
        buffering=Preloading data into a reserved area of memory
        The output was
        [wordsTableStream]: A, Ambulance
        [wordsTableStream]: B, Baby
         */
    }

    public static Topology GlobalKTableTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        GlobalKTable<String, String> wordsGlobalKTable =
                streamsBuilder.globalTable(WORDS_Topic, Consumed.with(Serdes.String(), Serdes.String()),
                        Materialized.as("words-global-store"));
        log.info("changing kTable to KStream.Because we can't print logs in kTable");
        String wordsGlobalKTableStream = wordsGlobalKTable.queryableStoreName();
        log.info("wordsGlobalKTableStream : {}", wordsGlobalKTableStream);
        // the output is  wordsGlobalKTableStream : words-global-store
        return streamsBuilder.build();
    }

}
