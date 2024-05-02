package com.kafka.advancedstreams.notes.WindowingAndTimeConceptsNotes;

public class ControllingEmissionOfIntermediateResults {
    /*
    1. So by default, aggregated window results are emitted based on the commit
   interval dot milliseconds.But the behavior that we are looking for is any time the
   defined window that we have for the window is completed, we want those results to be emitted downstream
  2.So in order for that to happen, there is an operator named Suppress in Kafka streams which can be used
   to suppress or buffer the records until the time interval for the window is complete.So this operator will buffer the records
   until the time interval for the window is complete.In order to achieve this, we need to provide three different configs.
  3. suppress is operator. To achieve this we need 3 configs

  // Suppressed.untilWindowCloses
    1. So this means when the window is closed, go ahead and emit the results to the downstream operators.

  // Suppressed.untilTimeLimit
    1. If there is no successive events for the defined time that's passed to it.
   Let's say if you pass the value as five seconds to it, in that case, if there are no events received
   with that five second interval, hen in that case it will emit the results downstream.

  BufferConfig.unbounded
   1. This represents unbounded memory to hold the records.If the memory is full then this will throw an out of memory exception.

  BufferFull Strategy
  emitEarlyWhenFull
  * So this will emit the results if the buffer is full downstream and this is going to make sure the app
    is still going to be up and processing the records But if the buffer is full, it's going to emit the results.
 * In tumblingWindow we observed whatever the aggregation that's happening, is emitting the
 results only after the commit interval is exhausted, right when the commit interval is exhausted or
 when the commit is performed, it emits all the windows that are created within this ten second interval.
  properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,"10000");

    wordsStream
 .groupByKey()
 .windowedBy(hoppingWindow)
 .count()
 .suppress(Suppressed
 .untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull())
 untilWindowCloses= emit the results any time the window closes. that window is five seconds
 BufferConfig.unbounded()=This means it can store in finite number of bytes. because others max records,
 max bytes  there is no way you can define this is how much of my buffer size is going to be.
 It is always recommended use unbounded
 shutDownWhenFull()=If the buffer is full, what action you need to take so you have shutdown when full.
   This is going to make sure are not going to lose any records.

   In o/p we can observe every 5 seconds records are emitting. our window size is 5 seconds.
   30,35--

   So now with the help of the Suppress operator, we are able to emit the results of the windowed aggregation
as per our window size. So this is one of the benefit of using the suppress operator.
The suppress operator takes control of when the results needs to be emitted downstream.
So by doing this, you are not tried to the commit interval millisecond value anymore.
And with this, the advantage here is that you get the aggregated data in the format you would expect
for the downstream operation.


 19:53:30.262 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- tumblingWindow : key : [A@1706106205000/1706106210000], value : 5
19:53:30.263 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- startLDT : 2024-01-24T19:53:25 , endLDT : 2024-01-24T19:53:30, Count : 5
[tumblingWindow]: [A@1706106205000/1706106210000], 5
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
19:53:35.318 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- tumblingWindow : key : [A@1706106210000/1706106215000], value : 5
19:53:35.319 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- startLDT : 2024-01-24T19:53:30 , endLDT : 2024-01-24T19:53:35, Count : 5
[tumblingWindow]: [A@1706106210000/1706106215000], 5
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
19:53:40.382 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- tumblingWindow : key : [A@1706106215000/1706106220000], value : 5
19:53:40.382 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- startLDT : 2024-01-24T19:53:35 , endLDT : 2024-01-24T19:53:40, Count : 5
[tumblingWindow]: [A@1706106215000/1706106220000], 5
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
19:53:45.428 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- tumblingWindow : key : [A@1706106220000/1706106225000], value : 5
19:53:45.428 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- startLDT : 2024-01-24T19:53:40 , endLDT : 2024-01-24T19:53:45, Count : 5
[tumblingWindow]: [A@1706106220000/1706106225000], 5
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
19:53:50.487 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- tumblingWindow : key : [A@1706106225000/1706106230000], value : 5
19:53:50.488 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- startLDT : 2024-01-24T19:53:45 , endLDT : 2024-01-24T19:53:50, Count : 5
[tumblingWindow]: [A@1706106225000/1706106230000], 5
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
19:53:55.551 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- tumblingWindow : key : [A@1706106230000/1706106235000], value : 5
19:53:55.551 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- startLDT : 2024-01-24T19:53:50 , endLDT : 2024-01-24T19:53:55, Count : 5
[tumblingWindow]: [A@1706106230000/1706106235000], 5
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
19:54:00.666 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store KSTREAM-AGGREGATE-STATE-STORE-0000000002.1706106240000 in regular mode
19:54:00.666 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- tumblingWindow : key : [A@1706106235000/1706106240000], value : 5
19:54:00.666 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- startLDT : 2024-01-24T19:53:55 , endLDT : 2024-01-24T19:54, Count : 5
[tumblingWindow]: [A@1706106235000/1706106240000], 5
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
19:54:05.699 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- tumblingWindow : key : [A@1706106240000/1706106245000], value : 5
19:54:05.699 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.ExploreSuppressOperatorTopology -- startLDT : 2024-01-24T19:54 , endLDT : 2024-01-24T19:54:05, Count : 5
[tumblingWindow]: [A@1706106240000/1706106245000], 5


     */
}
