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
public class SlidingWindowsTopology {

    public static final String WINDOW_WORDS_TOPIC = "windows-words";

    public static Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> wordsStream = streamsBuilder
                .stream(WINDOW_WORDS_TOPIC,
                        Consumed.with(Serdes.String(), Serdes.String()));
        wordsStream.print(Printed.<String, String>toSysOut().withLabel("words"));
        slidingWindows(wordsStream);
        return streamsBuilder.build();
    }

    private static void slidingWindows(KStream<String, String> wordsStream) {

        Duration windowSize = Duration.ofSeconds(5);

        SlidingWindows slidingWindows = SlidingWindows.ofTimeDifferenceWithNoGrace(windowSize);

        KTable<Windowed<String>, Long> windowedKTable = wordsStream
                .groupByKey()
                .windowedBy(slidingWindows)
                .count()
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));


        windowedKTable.toStream()
                .peek((key, value) -> {
                    log.info("slidingWindows : key : {}, value : {}", key, value);
                    printLocalDateTimes(key, value);
                })
                .print(Printed.<Windowed<String>, Long>toSysOut().withLabel("slidingWindows"));

    }

    private static void printLocalDateTimes(Windowed<String> key, Long value) {
        Instant startTime = key.window().startTime();
        Instant endTime = key.window().endTime();
        LocalDateTime startLDT = LocalDateTime.ofInstant(startTime, ZoneId.of(ZoneId.SHORT_IDS.get("IST")));
        LocalDateTime endLDT = LocalDateTime.ofInstant(endTime, ZoneId.of(ZoneId.SHORT_IDS.get("IST")));
        log.info("startLDT : {} , endLDT : {}, Count : {}", startLDT, endLDT, value);
    }

    /*
    o/p
    start window, this will match the time that we have the record being published from the data producer.
So if you go to the mock data producer and then if you search for this, we'll be able to see the first message.

if we compare mock data producer and sliding window timestamps match

22:35:12.564 = mock data
22:35:12.639 = first sliding window
22:35:12= time was match

22:35:13.565 = mock data
22:35:13.577 = second sliding window
22:35:13 = time was match

* These windows are derived from the event.It's not derived from the wall clock time.
* So it's the record time that drives the actual window creation

startLDT : 2024-01-24T22:35:07.523 , endLDT : 2024-01-24T22:35:12.523, Count : 1= 22:35:12.523-5 seconds= 2024-01-24T22:35:07.523
5 second SlidingWindows gap
start of the event 2024-01-24T22:35:12.523 - 5 second SlidingWindows= 2024-01-24T22:35:07.523 (12-5)=7
startLDT : 2024-01-24T22:35:08.565 , endLDT : 2024-01-24T22:35:13.565, Count : 2
observe for remaining.
count gradually goes up
we have the producer producing records every second and the windows are getting created for each and
every second

[words]: A, Apple
22:35:12.639 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store KSTREAM-AGGREGATE-STATE-STORE-0000000002.1706115900000 in regular mode
22:35:12.668 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115907523/1706115912523], value : 1
22:35:12.668 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:07.523 , endLDT : 2024-01-24T22:35:12.523, Count : 1
[slidingWindows]: [A@1706115907523/1706115912523], 1
[words]: A, Apple
22:35:13.577 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115908565/1706115913565], value : 2
22:35:13.577 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:08.565 , endLDT : 2024-01-24T22:35:13.565, Count : 2
[slidingWindows]: [A@1706115908565/1706115913565], 2
[words]: A, Apple
22:35:14.589 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115909579/1706115914579], value : 3
22:35:14.589 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:09.579 , endLDT : 2024-01-24T22:35:14.579, Count : 3
[slidingWindows]: [A@1706115909579/1706115914579], 3
[words]: A, Apple
22:35:15.612 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115910595/1706115915595], value : 4
22:35:15.612 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:10.595 , endLDT : 2024-01-24T22:35:15.595, Count : 4
[slidingWindows]: [A@1706115910595/1706115915595], 4
[words]: A, Apple
22:35:16.621 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115911609/1706115916609], value : 5
22:35:16.621 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:11.609 , endLDT : 2024-01-24T22:35:16.609, Count : 5
[slidingWindows]: [A@1706115911609/1706115916609], 5
[words]: A, Apple
22:35:17.636 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115912524/1706115917524], value : 4
22:35:17.636 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:12.524 , endLDT : 2024-01-24T22:35:17.524, Count : 4
[slidingWindows]: [A@1706115912524/1706115917524], 4
22:35:17.646 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115912627/1706115917627], value : 5
22:35:17.646 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:12.627 , endLDT : 2024-01-24T22:35:17.627, Count : 5
[slidingWindows]: [A@1706115912627/1706115917627], 5
[words]: A, Apple
22:35:18.662 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115913566/1706115918566], value : 4
22:35:18.662 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:13.566 , endLDT : 2024-01-24T22:35:18.566, Count : 4
[slidingWindows]: [A@1706115913566/1706115918566], 4
22:35:18.664 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115913651/1706115918651], value : 5
22:35:18.664 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:13.651 , endLDT : 2024-01-24T22:35:18.651, Count : 5
[slidingWindows]: [A@1706115913651/1706115918651], 5
[words]: A, Apple
22:35:19.678 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115914580/1706115919580], value : 4
22:35:19.679 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:14.580 , endLDT : 2024-01-24T22:35:19.580, Count : 4
[slidingWindows]: [A@1706115914580/1706115919580], 4
22:35:19.680 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115914666/1706115919666], value : 5
22:35:19.681 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:14.666 , endLDT : 2024-01-24T22:35:19.666, Count : 5
[slidingWindows]: [A@1706115914666/1706115919666], 5
[words]: A, Apple
22:35:20.693 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115915596/1706115920596], value : 4
22:35:20.694 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:15.596 , endLDT : 2024-01-24T22:35:20.596, Count : 4
[slidingWindows]: [A@1706115915596/1706115920596], 4
22:35:20.695 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115915684/1706115920684], value : 5
22:35:20.695 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:15.684 , endLDT : 2024-01-24T22:35:20.684, Count : 5
[slidingWindows]: [A@1706115915684/1706115920684], 5
[words]: A, Apple
22:35:21.709 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115916610/1706115921610], value : 4
22:35:21.709 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:16.610 , endLDT : 2024-01-24T22:35:21.610, Count : 4
[slidingWindows]: [A@1706115916610/1706115921610], 4
22:35:21.711 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- slidingWindows : key : [A@1706115916697/1706115921697], value : 5
22:35:21.711 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.SlidingWindowsTopology -- startLDT : 2024-01-24T22:35:16.697 , endLDT : 2024-01-24T22:35:21.697, Count : 5
[slidingWindows]: [A@1706115916697/1706115921697], 5



Mock Data producer data

22:35:12.564 [main] INFO com.kafka.advancedstreams.producer.WindowsMockDataProducer -- Published the alphabet message : windows-words-0@0
22:35:13.565 [main] INFO com.kafka.advancedstreams.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=windows-words, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=A, value=Apple, timestamp=null)
22:35:13.571 [main] INFO com.kafka.advancedstreams.producer.WindowsMockDataProducer -- Published the alphabet message : windows-words-0@1
22:35:14.579 [main] INFO com.kafka.advancedstreams.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=windows-words, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=A, value=Apple, timestamp=null)
22:35:14.585 [main] INFO com.kafka.advancedstreams.producer.WindowsMockDataProducer -- Published the alphabet message : windows-words-0@2
22:35:15.595 [main] INFO com.kafka.advancedstreams.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=windows-words, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=A, value=Apple, timestamp=null)
22:35:15.601 [main] INFO com.kafka.advancedstreams.producer.WindowsMockDataProducer -- Published the alphabet message : windows-words-0@3
22:35:16.609 [main] INFO com.kafka.advancedstreams.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=windows-words, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=A, value=Apple, timestamp=null)
22:35:16.615 [main] INFO com.kafka.advancedstreams.producer.WindowsMockDataProducer -- Published the alphabet message : windows-words-0@4
22:35:17.627 [main] INFO com.kafka.advancedstreams.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=windows-words, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=A, value=Apple, timestamp=null)
22:35:17.648 [main] INFO com.kafka.advancedstreams.producer.WindowsMockDataProducer -- Published the alphabet message : windows-words-0@5
22:35:18.651 [main] INFO com.kafka.advancedstreams.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=windows-words, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=A, value=Apple, timestamp=null)
22:35:18.664 [main] INFO com.kafka.advancedstreams.producer.WindowsMockDataProducer -- Published the alphabet message : windows-words-0@6
22:35:19.666 [main] INFO com.kafka.advancedstreams.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=windows-words, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=A, value=Apple, timestamp=null)
22:35:19.672 [main] INFO com.kafka.advancedstreams.producer.WindowsMockDataProducer -- Published the alphabet message : windows-words-0@7
22:35:20.684 [main] INFO com.kafka.advancedstreams.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=windows-words, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=A, value=Apple, timestamp=null)
22:35:20.688 [main] INFO com.kafka.advancedstreams.producer.WindowsMockDataProducer -- Published the alphabet message : windows-words-0@8
22:35:21.697 [main] INFO com.kafka.advancedstreams.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=windows-words, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=A, value=Apple, timestamp=null)
22:35:21.704 [main] INFO com.kafka.advancedstreams.producer.WindowsMockDataProducer -- Published the alphabet message : windows-words-0@9

     */

}
