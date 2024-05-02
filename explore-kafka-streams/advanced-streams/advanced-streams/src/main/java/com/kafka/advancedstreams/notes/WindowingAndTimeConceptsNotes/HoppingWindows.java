package com.kafka.advancedstreams.notes.WindowingAndTimeConceptsNotes;

public class HoppingWindows {
    /*
Duration windowSize = Duration.ofSeconds(5);
Duration advanceBySize = Duration.ofSeconds(3);

TimeWindows hoppingWindow = TimeWindows
 .ofSizeWithNoGrace(windowSize)
 .advanceBy(advanceBySize);
 advanceBy(advanceBySize)= window size, which is going to play the role of creating the overlapping windows

 representation of 5 second windows
  ww = window

  0 5 =ww1
  overlapping window = 3


             window2        window4         window6
   3(0+3)     (3+5) 8 9(6+3) (9+5)14 (16+3)19       24
     window1      window3     window5       window 7
 0          5  6       10 11         15 16        20



 * first window size here is also five seconds.So the first window is created with the five-second window
 and then all the aggregations will be performed based on the keys that are present in
 that five-second window.
 * The next one is going to be the overlapping window where the start of the window is going to be
three seconds from the starting window of the first windows start time(0+3)(3+5) = 3-8=window
9(6+3)- (9+5) 14= window
(16+3)19-(19+5)24 =window
above are overlapping window
here 3 is  advanceBy(advanceBySize) overlapping the reason for three seconds is because our advanced window size is three seconds and this process
continues for the remaining window buckets
5 is normal size window

* So the records are going to be overlapped between window buckets
* successive windows will be created by adding the advanced size from the previous windows starting
time.And then it continues to create the remaining window buckets.

o/p

[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
21:32:17.444 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- hoppingWindows : key : [A@1706112132000/1706112137000], value : 5
21:32:17.444 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- startLDT : 2024-01-24T21:32:12 , endLDT : 2024-01-24T21:32:17, Count : 5
[hoppingWindows]: [A@1706112132000/1706112137000], 5
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
21:32:20.473 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- hoppingWindows : key : [A@1706112135000/1706112140000], value : 5
21:32:20.474 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- startLDT : 2024-01-24T21:32:15 , endLDT : 2024-01-24T21:32:20, Count : 5
[hoppingWindows]: [A@1706112135000/1706112140000], 5
[words]: A, Apple
[words]: A, Apple
21:32:24.018 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- hoppingWindows : key : [A@1706112138000/1706112143000], value : 4
21:32:24.019 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- startLDT : 2024-01-24T21:32:18 , endLDT : 2024-01-24T21:32:23, Count : 4
[hoppingWindows]: [A@1706112138000/1706112143000], 4
[words]: A, Apple
[words]: A, Apple
21:32:26.043 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- hoppingWindows : key : [A@1706112141000/1706112146000], value : 3
21:32:26.043 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- startLDT : 2024-01-24T21:32:21 , endLDT : 2024-01-24T21:32:26, Count : 3
[hoppingWindows]: [A@1706112141000/1706112146000], 3
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
21:32:29.081 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- hoppingWindows : key : [A@1706112144000/1706112149000], value : 5
21:32:29.081 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- startLDT : 2024-01-24T21:32:24 , endLDT : 2024-01-24T21:32:29, Count : 5
[hoppingWindows]: [A@1706112144000/1706112149000], 5
[words]: A, Apple
[words]: A, Apple
[words]: A, Apple
21:32:32.120 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- hoppingWindows : key : [A@1706112147000/1706112152000], value : 5
21:32:32.120 [windows-1e544caf-2e49-4603-af9a-1a5db8716cea-StreamThread-1] INFO com.kafka.advancedstreams.topology.WondowsAndTimeConcepts.HoppingWindowTopology -- startLDT : 2024-01-24T21:32:27 , endLDT : 2024-01-24T21:32:32, Count : 5
[hoppingWindows]: [A@1706112147000/1706112152000], 5


above 29+3 = 32 overlapping window
     */
}
