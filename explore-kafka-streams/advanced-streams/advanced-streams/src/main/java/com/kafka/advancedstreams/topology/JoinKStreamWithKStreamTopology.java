package com.kafka.advancedstreams.topology;

import com.kafka.advancedstreams.domain.Alphabet;
import com.kafka.advancedstreams.serdes.SerdesFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;

@Slf4j
public class JoinKStreamWithKStreamTopology {
    public static String ALPHABETS_TOPIC = "alphabets"; // A => First letter in the english alphabet KTABLE

    public static String ALPHABETS_ABBREVIATIONS_TOPIC = "alphabets_abbreviations"; // A=> Apple KStream

    public static Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        //  JoinKStreamWithKStream(streamsBuilder);
        //  LeftJoinKStreamWithKStream(streamsBuilder);
        OuterJoinKStreamWithKStream(streamsBuilder);
        return streamsBuilder.build();
    }

    private static void JoinKStreamWithKStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> alphabetsAbbreviationStream =
                streamsBuilder.stream(ALPHABETS_ABBREVIATIONS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        log.info(" primary one stream is alphabetsAbbreviationStream");
        alphabetsAbbreviationStream.print(Printed.<String, String>toSysOut().withLabel("alphabets_abbreviations_stream"));

        KStream<String, String> alphabetsStream =
                streamsBuilder.stream(ALPHABETS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        alphabetsStream.print(Printed.<String, String>toSysOut().withLabel("alphabets_stream"));
        ValueJoiner<String, String, Alphabet> valueJoiner = Alphabet::new;

        log.info("join window. Duration we can provide anything like 1hr 1 day");
        log.info("The third parameter we need to pass kStream joining was join window. That is " +
                "different from all joins ");
        JoinWindows fiveSecondWindow = JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(5));
        log.info("The 4th parameter is  type of the join params. Same like consumed with" +
                "What the 1) key 2) value of 1st topic 3) value of 2nd topic");

        log.info("state stores that gets created here in join params. giving alphabets-join as name " +
                "and store name we are manually giving store names Instead kafka created by numbers");
        StreamJoined<String, String, String> joinedParams = StreamJoined
                .with(Serdes.String(), Serdes.String(), Serdes.String())
                .withName("alphabets-join")
                .withStoreName("alphabets-join");

        /*
        After giving store names the internal topics will be
     joins-alphabets-join-outer-other-join-store-changelog
      joins-alphabets-join-outer-shared-join-store-changelog
     joins-alphabets-join-outer-this-join-store-changelog
         */

        KStream<String, Alphabet> joinedStream =
                alphabetsAbbreviationStream.join(alphabetsStream, valueJoiner, fiveSecondWindow, joinedParams);
        joinedStream.print(Printed.<String, Alphabet>toSysOut().
                withLabel("alphabets_stream_alphabets_abbreviations_stream"));
        // The output will be
        /*
        [alphabets_stream]: B, B is the second letter in English Alphabets.
        [alphabets_stream]: A, A is the first letter in English Alphabets.
        [alphabets_abbreviations_stream]: B, Bus
[alphabets_stream_alphabets_abbreviations_stream]: B, Alphabet[abbreviation=Bus, description=B is the second letter in English Alphabets.]
[alphabets_abbreviations_stream]: A, Apple
[alphabets_stream_alphabets_abbreviations_stream]: A, Alphabet[abbreviation=Apple, description=A is the first letter in English Alphabets.]
[alphabets_abbreviations_stream]: B, Birthday.
[alphabets_stream_alphabets_abbreviations_stream]: B, Alphabet[abbreviation=Birthday., description=B is the second letter in English Alphabets.]
[alphabets_abbreviations_stream]: A, Ant
[alphabets_stream_alphabets_abbreviations_stream]: A, Alphabet[abbreviation=Ant, description=A is the first letter in English Alphabets.]


         */

    }

    private static void LeftJoinKStreamWithKStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> alphabetsAbbreviationStream =
                streamsBuilder.stream(ALPHABETS_ABBREVIATIONS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        log.info(" primary one stream is alphabetsAbbreviationStream");
        alphabetsAbbreviationStream.print(Printed.<String, String>toSysOut().withLabel("alphabets_abbreviations_stream"));

        KStream<String, String> alphabetsStream =
                streamsBuilder.stream(ALPHABETS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        alphabetsStream.print(Printed.<String, String>toSysOut().withLabel("alphabets_stream"));
        ValueJoiner<String, String, Alphabet> valueJoiner = Alphabet::new;

        log.info("join window. Duration we can provide anything like 1hr 1 day");
        log.info("The third parameter we need to pass kStream joining was join window. That is " +
                "different from all joins ");
        JoinWindows fiveSecondWindow = JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(5));
        log.info("The 4th parameter is  type of the join params. Same like consumed with" +
                "What the 1) key 2) value of 1st topic 3) value of 2nd topic");

        StreamJoined<String, String, String> joinedParams = StreamJoined
                .with(Serdes.String(), Serdes.String(), Serdes.String())
                .withName("alphabets-join")
                .withStoreName("alphabets-join");
        ;
        log.info("in place join replace left join");
        KStream<String, Alphabet> joinedStream =
                alphabetsAbbreviationStream.leftJoin(alphabetsStream, valueJoiner, fiveSecondWindow, joinedParams);
        joinedStream.print(Printed.<String, Alphabet>toSysOut().
                withLabel("alphabets_stream_alphabets_abbreviations_stream"));
        // The output will be
        /*
[alphabets_stream]: A, A is the first letter in English Alphabets.
[alphabets_stream]: C, C is the Third letter in English Alphabets.
[alphabets_stream]: B, B is the second letter in English Alphabets.
[alphabets_abbreviations_stream]: A, Apple
[alphabets_stream_alphabets_abbreviations_stream]: A, Alphabet[abbreviation=Apple, description=A is the first letter in English Alphabets.]
[alphabets_abbreviations_stream]: C, Cat
[alphabets_stream_alphabets_abbreviations_stream]: C, Alphabet[abbreviation=Cat, description=C is the Third letter in English Alphabets.]
[alphabets_abbreviations_stream]: B, Bus
[alphabets_stream_alphabets_abbreviations_stream]: B, Alphabet[abbreviation=Bus, description=B is the second letter in English Alphabets.]
[alphabets_abbreviations_stream]: T, Tiger
[alphabets_stream_alphabets_abbreviations_stream]: E, Alphabet[abbreviation=Elephant, description=null]
[alphabets_stream_alphabets_abbreviations_stream]: M, Alphabet[abbreviation=Monkey, description=null]
[alphabets_stream_alphabets_abbreviations_stream]: T, Alphabet[abbreviation=Tiger, description=null]
[alphabets_stream_alphabets_abbreviations_stream]: D, Alphabet[abbreviation=Dog, description=null]
[alphabets_abbreviations_stream]: E, Elephant
[alphabets_abbreviations_stream]: D, Dog
[alphabets_abbreviations_stream]: M, Monkey
[alphabets_abbreviations_stream]: B, Birthday.
[alphabets_stream_alphabets_abbreviations_stream]: B, Alphabet[abbreviation=Birthday., description=B is the second letter in English Alphabets.]
[alphabets_abbreviations_stream]: A, Ant
[alphabets_stream_alphabets_abbreviations_stream]: A, Alphabet[abbreviation=Ant, description=A is the first letter in English Alphabets.]

         */

    }


    private static void OuterJoinKStreamWithKStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> alphabetsAbbreviationStream =
                streamsBuilder.stream(ALPHABETS_ABBREVIATIONS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        log.info(" primary one stream is alphabetsAbbreviationStream");
        alphabetsAbbreviationStream.print(Printed.<String, String>toSysOut().withLabel("alphabets_abbreviations_stream"));

        KStream<String, String> alphabetsStream =
                streamsBuilder.stream(ALPHABETS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        alphabetsStream.print(Printed.<String, String>toSysOut().withLabel("alphabets_stream"));
        ValueJoiner<String, String, Alphabet> valueJoiner = Alphabet::new;

        log.info("join window. Duration we can provide anything like 1hr 1 day");
        log.info("The third parameter we need to pass kStream joining was join window. That is " +
                "different from all joins ");
        JoinWindows fiveSecondWindow = JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(30));
        log.info("The 4th parameter is  type of the join params. Same like consumed with" +
                "What the 1) key 2) value of 1st topic 3) value of 2nd topic");

        StreamJoined<String, String, String> joinedParams = StreamJoined
                .with(Serdes.String(), Serdes.String(), Serdes.String())
                .withName("alphabets-join")
                .withStoreName("alphabets-join");
        ;
        log.info("in place join replace outer join");
        KStream<String, Alphabet> joinedStream =
                alphabetsAbbreviationStream.outerJoin(alphabetsStream, valueJoiner, fiveSecondWindow, joinedParams);
        joinedStream.print(Printed.<String, Alphabet>toSysOut().
                withLabel("alphabets_stream_alphabets_abbreviations_stream"));
        // The output will be
        /*
        [alphabets_stream]: B, B is the second letter in English Alphabets.
[alphabets_stream_alphabets_abbreviations_stream]: z, Alphabet[abbreviation=null, description=Z is the twenty six letter in English Alphabets.]
[alphabets_stream_alphabets_abbreviations_stream]: F, Alphabet[abbreviation=null, description=F is the Six letter in English Alphabets.]
[alphabets_stream_alphabets_abbreviations_stream]: K, Alphabet[abbreviation=Knife, description=null]
[alphabets_stream_alphabets_abbreviations_stream]: C, Alphabet[abbreviation=Cat, description=null]
[alphabets_stream]: A, A is the first letter in English Alphabets.
[alphabets_abbreviations_stream]: A, Apple
[alphabets_stream_alphabets_abbreviations_stream]: A, Alphabet[abbreviation=Apple, description=A is the first letter in English Alphabets.]
[alphabets_abbreviations_stream]: B, Bus
[alphabets_stream_alphabets_abbreviations_stream]: B, Alphabet[abbreviation=Bus, description=B is the second letter in English Alphabets.]
[alphabets_abbreviations_stream]: C, Cat
[alphabets_abbreviations_stream]: B, Birthday.
[alphabets_stream_alphabets_abbreviations_stream]: B, Alphabet[abbreviation=Birthday., description=B is the second letter in English Alphabets.]
[alphabets_abbreviations_stream]: A, Ant
[alphabets_stream_alphabets_abbreviations_stream]: A, Alphabet[abbreviation=Ant, description=A is the first letter in English Alphabets.]
         */

    }


}
