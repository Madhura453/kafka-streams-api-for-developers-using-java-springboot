package com.kafka.advancedstreams.topology.WondowsAndTimeConcepts;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
public class HoppingWindowTopology {

    public static final String WINDOW_WORDS_TOPIC = "windows-words";

    public static Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> wordsStream = streamsBuilder
                .stream(WINDOW_WORDS_TOPIC,
                        Consumed.with(Serdes.String(), Serdes.String()));
        wordsStream.print(Printed.<String, String>toSysOut().withLabel("words"));
        hoppingWindows(wordsStream);
        return streamsBuilder.build();
    }

    private static void hoppingWindows(KStream<String, String> wordsStream) {

        Duration windowSize = Duration.ofSeconds(5);

        Duration advancedWindowSize = Duration.ofSeconds(3);

        TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(windowSize)
                .advanceBy(advancedWindowSize);// It is overlapping window

        KTable<Windowed<String>, Long> windowedKTable = wordsStream
                .groupByKey()
                .windowedBy(timeWindows)
                .count()
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

        log.info("stores infinite's number records when memory was full shutdown the app. after window time  exhausted" +
                "emit the records");
        log.info("we are just printing this value only when the window is exhausted");
        windowedKTable.toStream()
                .peek((key, value) -> {
                    log.info("hoppingWindows : key : {}, value : {}", key, value);
                    printLocalDateTimes(key, value);
                })
                .print(Printed.<Windowed<String>, Long>toSysOut().withLabel("hoppingWindows"));

    }

    private static void printLocalDateTimes(Windowed<String> key, Long value) {
        Instant startTime = key.window().startTime();
        Instant endTime = key.window().endTime();
        LocalDateTime startLDT = LocalDateTime.ofInstant(startTime, ZoneId.of(ZoneId.SHORT_IDS.get("IST")));
        LocalDateTime endLDT = LocalDateTime.ofInstant(endTime, ZoneId.of(ZoneId.SHORT_IDS.get("IST")));
        log.info("startLDT : {} , endLDT : {}, Count : {}", startLDT, endLDT, value);
    }

}
