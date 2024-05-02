package com.kafka.advancedstreams.notes.WindowingAndTimeConceptsNotes;

public class TumblingWindowNotes {
    /*
    1. you define a duration of days one, and then have this app continuously running and it's going to automatically create the aggregated data
that we are looking for this whole day time interval.So tumbling window always relates to the apps running wall clocks machine.
So let's say if you're running this in a Unix machine, it's going to take the machines time and then create this duration of one day
from that machine's time.
 2. each window that's created is going to have a start time and end time And it is of type instant
 3.
  1. tumblingWindow : key : [A@1706072110000/1706072115000], value : 2
    1706072110000=from timestamp,  to timestamp=1706072115000
    this one is our system local timestamp
  startLDT : 2024-01-24T10:25:10 , endLDT : 2024-01-24T10:25:15, Count : 2
    2. So the start time and end time of this window is created based on the wall clock time of the apps running
   machine.
   3. in system, these time intervals are created based on the time interval(5) that we defined.
   4. The defined interval is five seconds
   5. That's why you have the time interval created as five second intervals and it always starts with the
     zeroth second and ends with the fifth second for the first interval and the second interval created
    as starts with the fifth, second, and then ends with the zeroth second.

     oneWindow= startLDT : 2024-01-24T10:25:10 , endLDT : 2024-01-24T10:25:15, Count : 2
                 startLDT : 2024-01-24T10:25:10 , endLDT : 2024-01-24T10:25:15, Count : 5
                 2 because of our commit interval 10 seconds=
                 properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,"10000");
    SecondWindow=  startLDT : 2024-01-24T10:25:15 , endLDT : 2024-01-24T10:25:20, Count : 2
                   startLDT : 2024-01-24T10:25:20 , endLDT : 2024-01-24T10:25:25, Count : 5
   ThirdWindow= startLDT : 2024-01-24T10:25:25 , endLDT : 2024-01-24T10:25:30, Count : 5
   FourthWindow= startLDT : 2024-01-24T10:25:30 , endLDT : 2024-01-24T10:25:35, Count : 2
                 startLDT : 2024-01-24T10:25:30 , endLDT : 2024-01-24T10:25:35, Count : 5
    fifthWindow=startLDT : 2024-01-24T10:25:35 , endLDT : 2024-01-24T10:25:40, Count : 5

    see above every 5 seconds window count operation done. It was counting the records every 5 seconds.
    This interval will continue when we get new records

   6. So tumbling window creates a time interval based on the duration that you defined, and it is also based
on the running machine's wall clock time, and it always starts with the zeroth second, and then it
continues to create those windows for the future events that comes in this given Kafka topic.
So now where is this timestamp coming from when you look at the Kafka producer, we are creating the producer record.
the default timestamp extractor is I believe it's fail on invalid
we are just publishing that record the default timestamp when you publish the record is system timestamp.

tumblingWindow : key : [A@1706072130000/1706072135000], value : 2
startLDT : 2024-01-24T10:25:30 , endLDT : 2024-01-24T10:25:35, Count : 2

This timestamp was coming from we are just publishing that record the default timestamp when
you publish the record is system timestamp.

0:21:06.173 [main] INFO org.apache.kafka.streams.StreamsConfig -- StreamsConfig values:
default.timestamp.extractor = class org.apache.kafka.streams.processor.FailOnInvalidTimestamp
go this class FailOnInvalidTimestamp
@Override
    public long extract(final ConsumerRecord<Object, Object> record, final long partitionTime) {
        final long timestamp = record.timestamp();

        if (timestamp < 0) {
            return onInvalidTimestamp(record, timestamp, partitionTime);
        }

        return timestamp;
    }

from here default producer get the timestamp. So record dot Timestamp is going to return the record timestamp and
then assign those records into this particular time window.
Mock data producer is actually publishing the record with a gap of every second
* So when we produce a data, so even though we are not passing the actual timestamp over here, the timestamp
  gets added before it gets published into the Kafka topic.So in here,
  before this record gets published, it's going to take the current machine's timestampand then add that to
  that record and then publish that to the Kafka topic.


* [tumblingWindow]: [A@1706072120000/1706072125000]=basically the windowed keyed start time
and windowed keyed end time, these are in GMT format
* you always pass the GMT time in order to query against this data

o/p tumblingWindow

[words]: A, Apple
[words]: A, Apple
10:25:11.746 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store KSTREAM-AGGREGATE-STATE-STORE-0000000002.1706072100000 in regular mode
10:25:11.778 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072110000/1706072115000], value : 2
10:25:11.778 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:10 , endLDT : 2024-01-24T10:25:15, Count : 2
[tumblingWindow]: [A@1706072110000/1706072115000], 2
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
10:25:21.825 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072110000/1706072115000], value : 5
10:25:21.825 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:10 , endLDT : 2024-01-24T10:25:15, Count : 5
[tumblingWindow]: [A@1706072110000/1706072115000], 5
10:25:21.826 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072115000/1706072120000], value : 2
10:25:21.826 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:15 , endLDT : 2024-01-24T10:25:20, Count : 2
[tumblingWindow]: [A@1706072115000/1706072120000], 2
10:25:21.827 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072120000/1706072125000], value : 2
10:25:21.827 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:20 , endLDT : 2024-01-24T10:25:25, Count : 2
[tumblingWindow]: [A@1706072120000/1706072125000], 2
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
10:25:31.865 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072120000/1706072125000], value : 5
10:25:31.866 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:20 , endLDT : 2024-01-24T10:25:25, Count : 5
[tumblingWindow]: [A@1706072120000/1706072125000], 5
10:25:31.866 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072125000/1706072130000], value : 5
10:25:31.866 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:25 , endLDT : 2024-01-24T10:25:30, Count : 5
[tumblingWindow]: [A@1706072125000/1706072130000], 5
10:25:31.868 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072130000/1706072135000], value : 2
10:25:31.868 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:30 , endLDT : 2024-01-24T10:25:35, Count : 2
[tumblingWindow]: [A@1706072130000/1706072135000], 2
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
10:25:51.912 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072140000/1706072145000], value : 5
10:25:51.912 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:40 , endLDT : 2024-01-24T10:25:45, Count : 5
[tumblingWindow]: [A@1706072140000/1706072145000], 5
10:25:51.913 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072145000/1706072150000], value : 5
10:25:51.913 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:45 , endLDT : 2024-01-24T10:25:50, Count : 5
[tumblingWindow]: [A@1706072145000/1706072150000], 5
10:25:51.913 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072150000/1706072155000], value : 2
10:25:51.913 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:50 , endLDT : 2024-01-24T10:25:55, Count : 2
[tumblingWindow]: [A@1706072150000/1706072155000], 2
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
10:26:01.970 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072150000/1706072155000], value : 5
10:26:01.970 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:50 , endLDT : 2024-01-24T10:25:55, Count : 5
[tumblingWindow]: [A@1706072150000/1706072155000], 5
10:26:01.970 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072155000/1706072160000], value : 5
10:26:01.971 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:55 , endLDT : 2024-01-24T10:26, Count : 5
[tumblingWindow]: [A@1706072155000/1706072160000], 5
10:26:02.062 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store KSTREAM-AGGREGATE-STATE-STORE-0000000002.1706072160000 in regular mode
10:26:02.063 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072160000/1706072165000], value : 2
10:26:02.063 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:26 , endLDT : 2024-01-24T10:26:05, Count : 2
[tumblingWindow]: [A@1706072160000/1706072165000], 2



 properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,"10000"); 10000 ms=10s

 1. The window data, the window aggregated data is emitted every ten seconds.
  It's because these windows are created and sent to the downstream operator.(in our case it was
  print statement .print(Printed.<Windowed<String>,Long>toSysOut().withLabel("tumblingWindow"));
2.  basically connected to the commit time interval, let's say if the time window is of 30 seconds or if it's of
  30 minutes even in that case, the aggregated data is going to be printed every ten seconds.

  observed below o/p 10:25:31.865 , 10:25:41.879, 10:25:51.913, 10:26:01.970
  every 10 seconds 31,41,51,01= the aggregated data is going to be printing

3. So that's not the actual behavior that we expect for the most part, because we would like to always
  want to know the aggregated data based on the time interval that you define. it will be next task
 4.


  10:25:31.865 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072120000/1706072125000], value : 5
10:25:31.866 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:20 , endLDT : 2024-01-24T10:25:25, Count : 5
[tumblingWindow]: [A@1706072120000/1706072125000], 5
10:25:31.866 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072125000/1706072130000], value : 5
10:25:31.866 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:25 , endLDT : 2024-01-24T10:25:30, Count : 5
[tumblingWindow]: [A@1706072125000/1706072130000], 5
10:25:31.868 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072130000/1706072135000], value : 2
10:25:31.868 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:30 , endLDT : 2024-01-24T10:25:35, Count : 2
[tumblingWindow]: [A@1706072130000/1706072135000], 2
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
10:25:51.912 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072140000/1706072145000], value : 5
10:25:51.912 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:40 , endLDT : 2024-01-24T10:25:45, Count : 5
[tumblingWindow]: [A@1706072140000/1706072145000], 5
10:25:51.913 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072145000/1706072150000], value : 5
10:25:51.913 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:45 , endLDT : 2024-01-24T10:25:50, Count : 5
[tumblingWindow]: [A@1706072145000/1706072150000], 5
10:25:51.913 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072150000/1706072155000], value : 2
10:25:51.913 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:50 , endLDT : 2024-01-24T10:25:55, Count : 2
[tumblingWindow]: [A@1706072150000/1706072155000], 2
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
10:26:01.970 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072150000/1706072155000], value : 5
10:26:01.970 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:50 , endLDT : 2024-01-24T10:25:55, Count : 5
[tumblingWindow]: [A@1706072150000/1706072155000], 5
10:26:01.970 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072155000/1706072160000], value : 5
10:26:01.971 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:25:55 , endLDT : 2024-01-24T10:26, Count : 5
[tumblingWindow]: [A@1706072155000/1706072160000], 5
10:26:02.062 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store KSTREAM-AGGREGATE-STATE-STORE-0000000002.1706072160000 in regular mode
10:26:02.063 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- tumblingWindow : key : [A@1706072160000/1706072165000], value : 2
10:26:02.063 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreWindowTopology -- startLDT : 2024-01-24T10:26 , endLDT : 2024-01-24T10:26:05, Count : 2
[tumblingWindow]: [A@1706072160000/1706072165000], 2
     */
}
