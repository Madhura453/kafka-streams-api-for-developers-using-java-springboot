package com.kafka.advancedstreams.notes;

public class PreconditionsJoins {
    /*
   // prerequisite or preconditions for performing joins
   1. No of partitions = no of tasks get created
   2.if preconditions not satisfied we can use e select key or map operator to rekey the records so that
    it meets the necessary requirements
    // no partitions should be equal for joins = why?
    1. Because different partitions will be different tasks will be created. The records will be distributed
    among different partitions.
    2. let's say 1st topic 2 partitions 2 tasks. 3 records produced.
     2 record in 1 st partition  3 record in 2nd  partition

     2nd topic 1 partition so 1 tasks 3 records will be in 1 partition only. so join will not happened.
     */

    /*

   // KStream-GlobalKTable= No of partitions will be different also join will happen why?
    1. GlobalKTable have access to all application id instances. It has access to all instances.
     */
}
