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
public class ExploreTumblingWindowTopology {

    public static final String WINDOW_WORDS_TOPIC = "windows-words";

    public static Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> wordsStream = streamsBuilder
                .stream(WINDOW_WORDS_TOPIC,
                        Consumed.with(Serdes.String(), Serdes.String()));
        wordsStream.print(Printed.<String, String>toSysOut().withLabel("words"));
        tumblingWindow(wordsStream);
        return streamsBuilder.build();
    }

    private static void tumblingWindow(KStream<String, String> wordsStream) {
        log.info("we are going to be working on building our logic to create a tumbling window and " +
                "then perform the count aggregation by reading the data from this window");
        log.info("we are going to do a count aggregation, " +
                "but that count aggregation is going to be windowed by the tumbling window");
        Duration windowSize = Duration.ofSeconds(5);
        log.info("time window Building");
        TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(windowSize);
        log.info("Build our aggregation logic. Aggregation logic is to count the number of keys");
        log.info("windowedBy is going to take care of creating the windows for you");
        log.info(" then for each window, go ahead and perform the count operation");
        log.info("So this is going to take care of performing the aggregation on each and every window that's " +
                "defined by this window size 5 seconds");

       TimeWindowedKStream<String, String> timeWindowedKStream = wordsStream
                .groupByKey()
                .windowedBy(timeWindows)
               // This is going to take care of creating the windows for you.
                ;
       log.info("windowed stream is going to be the key of this windowed by operation");

        KTable<Windowed<String>, Long> windowedKTable= timeWindowedKStream.count();

        windowedKTable.toStream()
                .peek((key, value) -> {
                   // log.info("The timestamp of each and every window that gets created");
                    // each window that's created is going to have a start time and end time And
                    // it is of type instant
                    log.info("tumblingWindow : key : {}, value : {}", key, value);
                    printLocalDateTimes(key,value);
                })
                .print(Printed.<Windowed<String>,Long>toSysOut().withLabel("tumblingWindow"));
/*
the o/p

[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
10:25:41.879 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072130000/1706072135000], value : 5
10:25:41.879 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:30 , endLDT : 2024-01-24T10:25:35, Count : 5
[tumblingWindow]: [A@1706072130000/1706072135000], 5
10:25:41.879 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072135000/1706072140000], value : 5
10:25:41.879 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:35 , endLDT : 2024-01-24T10:25:40, Count : 5
[tumblingWindow]: [A@1706072135000/1706072140000], 5
10:25:41.880 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072140000/1706072145000], value : 2
10:25:41.880 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:40 , endLDT : 2024-01-24T10:25:45, Count : 2
[tumblingWindow]: [A@1706072140000/1706072145000], 2
 */

    }

    private static void printLocalDateTimes(Windowed<String> key, Long value) {
//        log.info("Each window that's created is going to have a start time and end time and it is of type instant"+
//                " converting that instant value to a local timestamp");
//        log.info("The timestamp of each and every window that gets created in local timestamp ");
        Instant startTime = key.window().startTime();
        Instant endTime = key.window().endTime();
        LocalDateTime startLDT = LocalDateTime.ofInstant(startTime, ZoneId.of(ZoneId.SHORT_IDS.get("IST")));
        LocalDateTime endLDT = LocalDateTime.ofInstant(endTime, ZoneId.of(ZoneId.SHORT_IDS.get("IST")));
        log.info("startLDT : {} , endLDT : {}, Count : {}", startLDT, endLDT, value);
    }

}
