package com.kafka.advancedstreams.topology;

import com.kafka.advancedstreams.AggregationOperators.AggregationOperators;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

@Slf4j
public class ExploreAggregateOperatorsTopology {
    public static String AGGREGATE_TOPIC = "aggregate";

    public static Topology buildTopologyByGroupByKey() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> inputStream = streamsBuilder.stream(AGGREGATE_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));
        inputStream.print(Printed.<String, String>toSysOut().withLabel("inputStream"));
        log.info("performing grouping on input stream");
        log.info("groupByKey going to automatically take the key from the actual record and " + "then apply grouping on it");
        KGroupedStream<String, String> groupedStream = inputStream.groupByKey(Grouped.with(Serdes.String(), Serdes.String()));
       // AggregationOperators.exploreCount(groupedStream);
       // AggregationOperators.exploreReduce(groupedStream);
       // AggregationOperators.exploreAggregate(groupedStream);
        AggregationOperators.exploreCountWithMaterialized(groupedStream);
        AggregationOperators.exploreReduceWithMaterialized(groupedStream);
        return streamsBuilder.build();
    }

    public static Topology buildTopologyByGroupBy() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> inputStream = streamsBuilder.stream(AGGREGATE_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));
        inputStream.print(Printed.<String, String>toSysOut().withLabel("inputStream"));
        log.info("groupBy used for custom key. We can change the key value what ever we need");
        log.info("Here key is key");
        KGroupedStream<String, String> groupedStream = inputStream.groupBy((key, value) ->
                value, Grouped.with(Serdes.String(), Serdes.String()));
        // Bus, 5  Apple, 5 Alligator, 5 Ambulance, 5 Baby, 5
        AggregationOperators.exploreCount(groupedStream);
        return streamsBuilder.build();

    }

}
