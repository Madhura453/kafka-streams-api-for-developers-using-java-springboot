package com.kafka.advancedstreams.topology;

import com.kafka.advancedstreams.domain.Alphabet;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

@Slf4j
public class ExploreJoinsOperatorsTopology {

    // KTAble is ALPHABETS because alphabets doesn't change
    public static String ALPHABETS_TOPIC = "alphabets"; // A => First letter in the english alphabet KTABLE

    // kstream is ABBREVIATIONS because it will change
    public static String ALPHABETS_ABBREVIATIONS_TOPIC = "alphabets_abbreviations"; // A=> Apple KStream

    public static Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
           joinKStreamWithKTable(streamsBuilder);
         //  joinKStreamWithGlobalKTable(streamsBuilder);
         // joinKTables(streamsBuilder);
        return streamsBuilder.build();
    }

    public static void joinKStreamWithKTable(StreamsBuilder streamsBuilder) {
        log.info("kStream for alphabets_abbreviations");

        KStream<String, String> alphabetsAbbreviationStream =
                streamsBuilder.stream(ALPHABETS_ABBREVIATIONS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        alphabetsAbbreviationStream.print(Printed.<String, String>toSysOut().withLabel("alphabets_abbreviations_stream"));
        log.info("creating Ktable for ALPHABETS");

        KTable<String, String> alphabetsTable =
                streamsBuilder.table(ALPHABETS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()), Materialized.as("alphabets-store"));

        alphabetsTable.toStream().print(Printed.<String, String>toSysOut().withLabel("alphabets_table"));
        ValueJoiner<String, String, Alphabet> valueJoiner = Alphabet::new;
        log.info("join operation starts");
        log.info("value from the those 2 instances combined into Alphabet model. Key doesn't change");
        KStream<String, Alphabet> joinedStream = alphabetsAbbreviationStream.join(alphabetsTable, valueJoiner);
        // The ouput of join looks like this
        //[alphabets-with-abbreviations]: A, Alphabet[abbreviation=Apple, description=A is the first letter in English Alphabets.]
        //[alphabets-with-abbreviations]: B, Alphabet[abbreviation=Bus, description=B is the second letter in English Alphabets.]
        joinedStream.print(Printed.<String, Alphabet>toSysOut().withLabel("alphabets-with-abbreviations"));
        /*
       1. Each time when we published the message in kstream topic. It will look into matching alphabet in
       KTable.
       2. KTable doesn't look for matching key. 3. KStream will look for matching key.
       3. We have the updated data in kTable, but if you take a look at it,
       it doesn't trigger the join at all because When this event is received, it's not going to look for a matching key
       or something in the K stream.
         */
    }


    public static void joinKStreamWithGlobalKTable(StreamsBuilder streamsBuilder) {
        log.info("kStream for alphabets_abbreviations");

        KStream<String, String> alphabetsAbbreviationStream =
                streamsBuilder.stream(ALPHABETS_ABBREVIATIONS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        alphabetsAbbreviationStream.print(Printed.<String, String>toSysOut().withLabel("alphabets_abbreviations_stream"));
        log.info("creating GlobalKTable for ALPHABETS");

        GlobalKTable<String, String> alphabetsGlobalTable =
                streamsBuilder.globalTable(ALPHABETS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()), Materialized.as("alphabets-store"));

        ValueJoiner<String, String, Alphabet> valueJoiner = Alphabet::new;
        log.info(" going to use the key that's going to be coming from the Alphabets abbreviation Kafka topic.");
        KeyValueMapper<String, String, String> keyValueMapper = (leftKey, rightKey) -> rightKey;
        KStream<String, Alphabet> joinedStream =
                alphabetsAbbreviationStream.join(alphabetsGlobalTable, keyValueMapper, valueJoiner);
        joinedStream.print(Printed.<String, Alphabet>toSysOut().withLabel("alphabets-with-abbreviations"));
    }

    public static void joinKTables(StreamsBuilder streamsBuilder) {
        log.info("creating KTable for alphabets_abbreviations");

        KTable<String, String> alphabetsAbbreviationTable =
                streamsBuilder.table(ALPHABETS_ABBREVIATIONS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()),
                        Materialized.as("alphabets-abbreviations-store"));

        alphabetsAbbreviationTable.toStream().print(Printed.<String, String>toSysOut().withLabel("alphabets_abbreviations_table"));
        log.info("creating Ktable for ALPHABETS");

        KTable<String, String> alphabetsTable =
                streamsBuilder.table(ALPHABETS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()),
                        Materialized.as("alphabets-store"));

        alphabetsTable.toStream().print(Printed.<String, String>toSysOut().withLabel("alphabets_table"));
        ValueJoiner<String, String, Alphabet> valueJoiner = Alphabet::new;
        KTable<String, Alphabet> joinedStream = alphabetsAbbreviationTable.join(alphabetsTable, valueJoiner);
        joinedStream.toStream().print(Printed.<String, Alphabet>toSysOut().withLabel("alphabets-with-abbreviations"));
        // output
        //        [alphabets-with-abbreviations]: B, Alphabet[abbreviation=Bus, description=B is the SECOND letter in English Alphabets.]
        //[alphabets-with-abbreviations]: A, Alphabet[abbreviation=Apple, description=A is the FIRST letter in English Alphabets.]
        //[alphabets-with-abbreviations]: B, Alphabet[abbreviation=Bus, description=B is the SECOND letter in English Alphabets.]
        //[alphabets-with-abbreviations]: A, Alphabet[abbreviation=Apple, description=A is the FIRST letter in English Alphabets.]
        // Because from 1st topic=2nd topic 2
        // 2nd topic to first topic = 2 total 4
        /*
        Alphabet with abbreviation printed four times here.Because both the side of the data
        is actually triggering a join.
         */
    }


}
