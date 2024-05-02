package com.kafka.advancedstreams.notes.WindowingAndTimeConceptsNotes;

public class SlidingWindowsNotes {
    /*
SlidingWindows slidingWindow = SlidingWindows
 .ofTimeDifferenceWithNoGrace(Duration.ofSeconds(5));

 in PDf we look into that diagram
 1. representation of the timeline in seconds here and we have the sliding window duration of five
seconds.
SlidingWindowDuration=5
receive an event at the sixth second.
a sliding window will be created with a event minus the time defined in the sliding window.

receive event in 6th second
sliding window will be (6-SlidingWindowDuration(5 in our case) =1

1  - receive event in 6th second = sliding window

receive event = 7th second

Sliding window will be (7-5) =2

2  - receive event in 7th second = sliding window

1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 20 21 22 23
in seconds

* It's an event that drives the window bucket.
* that windows that are created can overlap if there are events within the defined window
* that the start time and end time of these window bucket is inclusive which means if we receive a record at the second of the starting window, at the second of the end
 window, those will also be included as part of the aggregation.

// use cases
1. create windows in small increments of time
2. if you are not going to be reliant on the machine's clock time and you want
  the events to drive the actual time windows, then in those kind of scenarios we can use the sliding window
     */
}
