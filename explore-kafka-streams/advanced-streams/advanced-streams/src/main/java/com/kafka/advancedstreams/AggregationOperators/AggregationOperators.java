package com.kafka.advancedstreams.AggregationOperators;

import com.kafka.advancedstreams.domain.AlphabetWordAggregate;
import com.kafka.advancedstreams.serdes.SerdesFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

@Slf4j
public class AggregationOperators {

    public static void exploreCount(KGroupedStream<String, String> groupedStream) {
        log.info("groupedStream converted to kTable");
        KTable<String, Long> countByAlphabet = groupedStream.count(Named.as("count-per-alphabet"));
        countByAlphabet.toStream()
                .print(Printed.<String, Long>toSysOut().withLabel("words_count_per_alphabeat"));// A, 6 B, 4

    }

    public static void exploreCountWithMaterialized(KGroupedStream<String, String> groupedStream) {
        log.info("groupedStream converted to kTable");
        KTable<String, Long> countByAlphabet = groupedStream.count(Named.as("count-per-alphabet"),
                Materialized.as("count-per-alphabet"));
        // Materialized.as defining a state-store and saving the state data in rocksDB and changelog topic
        countByAlphabet.toStream()
                .print(Printed.<String, Long>toSysOut().withLabel("words_count_per_alphabeat"));// A, 6 B, 4

    }

    public static void exploreReduce(KGroupedStream<String, String> groupedStream) {
        log.info("reduce is multiple events into single event");
        log.info("value 1 is previous value and value 2 is current value");
        KTable<String, String> reducedStream = groupedStream.reduce(((value1, value2) -> {
            log.info("value1 : {} , value2 : {} ", value1, value2);
            return value1.toUpperCase() + "-" + value2.toUpperCase();// A- Apple-Alligator so on
        }));
        reducedStream.toStream().print(Printed.<String, String>toSysOut().withLabel("reduced-words"));
        // A, APPLE-ALLIGATOR-AMBULANCE  B, BUS-BABY
    }

    public static void exploreReduceWithMaterialized(KGroupedStream<String, String> groupedStream) {
        log.info("reduce is multiple events into single event");
        log.info("value 1 is previous value and value 2 is current value");
        KTable<String, String> reducedStream = groupedStream.reduce((value1, value2) -> {
                    log.info("value1 : {} , value2 : {} ", value1, value2);
                    return value1.toUpperCase() + "-" + value2.toUpperCase();
                }
                , Materialized
                        .<String, String, KeyValueStore<Bytes, byte[]>>as("reduced-words")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.String())
        );
        reducedStream.toStream().print(Printed.<String, String>toSysOut().withLabel("reduced-words"));
    }

    public static void exploreAggregate(KGroupedStream<String, String> groupedStream) {
        log.info("alphabetWordAggregateInitializer uses to hold aggregate result");
        Initializer<AlphabetWordAggregate> alphabetWordAggregateInitializer = AlphabetWordAggregate::new;
        log.info("Aggregator is going to be the code, which is going to perform the actual aggregation");
        log.info("aggregator key is string and value is string and output is  AlphabetWordAggregate");
        // (key, value, aggregate)  it is actual records from grouped stream
        // key=string, value=string, aggregate=AlphabetWordAggregate
        Aggregator<String, String, AlphabetWordAggregate> aggregator = (key, value, aggregate) ->
                aggregate.updateEvents(key, value);
        log.info("since we are changing the type from one to another because we're changing the type from the actual string to the alphabet word aggregate, \n" +
                "the better option in this case is to provide the materialized view.");
        log.info("So materialized view is one of the option of you providing your own state store instead of Kafka, " +
                "taking care of creating that for you");

        KTable<String, AlphabetWordAggregate> aggregatedStream = groupedStream.aggregate(alphabetWordAggregateInitializer, aggregator,
                Materialized.<String, AlphabetWordAggregate, KeyValueStore<Bytes, byte[]>>as("aggregated-words")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(SerdesFactory.AlphabetWordAggregateSerde())
        );

        aggregatedStream
                .toStream()
                .print(Printed.<String, AlphabetWordAggregate>toSysOut().withLabel("aggregated-Words"));
        /*
        o/p =
        [aggregated-Words]: A, AlphabetWordAggregate[key=A, valueSet=[Apple, Alligator, Ambulance], runningCount=6]
        [aggregated-Words]: B, AlphabetWordAggregate[key=B, valueSet=[Bus, Baby], runningCount=4]
         */
    }
}

/*
 Initializer<AlphabetWordAggregate> alphabetWordAggregateInitializer = AlphabetWordAggregate::new;
 If you start the application first time then it will be empty record
Materialized.<String, AlphabetWordAggregate, KeyValueStore<Bytes, byte[]>>as("aggregated-words")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(SerdesFactory.AlphabetWordAggregateSerde()
 key= is string
value is= AlphabetWordAggregate
what kind of data that store stores= i.e is key in bytes and value in byteArray
 */