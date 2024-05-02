package com.kafka.advancedstreams.notes;

public class KStreamToKStreamJoinNotes {
    /*
    1. Kstream is infinite stream. So It required time window to join.
    * . For joining, it has 2 requires Number one is key and number two is there is a time window defined
     within the time window
     * So by default, any record that gets produced into the Kafka topic gets a timestamp attached to it.

     // K stream to K stream join works
     1. So in this case, I'm going to assume the kStream one is primary and KStream two is the secondary stream and
     we are trying to perform a join between these two streams. As you can see in the example, they both have the key.
     A Now let's say we add a time window of five seconds.So in this case the timestamp in the primary one defines a starting window.
      so let's say the time is 5 p.m. and the second is 00.If the second event, which is in this case KStream two, if an event is
       received before and after of the 5 p.m. and zero second, which in this case is 5 p.m., 204 second or 4 p.m., 59th minute
       and 56 seconds.If a record that falls between those two windows, then those records will be joined. So in those kind of
       scenarios, a join happens and we can create a aggregated data from these two KStreams.

     2. So there are two things.Number one is they should share the same key and there should be a time window and these records
     between these two instances should be part of that defined time window.

    //  JoinWindows

      1. JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(5));
      2. The record can be back and 4th of the starting window. will trigger join





     */
}
