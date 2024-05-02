package com.kafka.advancedstreams.notes;

public class JoinOperationsNotes {
    /*
    same like SQL joins
    ValueJoiner
    * To combine data into 2 topics joins data we need value joiner.
    * It is mandatory in joins to combine data from 2 topics
    * it takes 3 arguments 1) value of first topic datatype 2) value of second topic
    3) output of joins
    // joinKStreamWithKTable
    1. in the case of join kStream with kTable, new events into the kTable doesn't trigger any join, but new events
    into the kStream will always trigger a join if there is a matching key found in the alphabets table.

    // joinKStreamWithGlobalKTable
    1. there is no toStream method in globalKTable to print result
    2.need to provide one additional thing anytime you are joining a global key table.
    3. The reason being a global table is a representation of all the data That's part of the Kafka topic.It's not about a specific instance holding
     a set of keys based on the partition that particular task interacts with.It's going to have the whole representation.
     So in those kind of scenarios, you need to provide a key value mapper that's going to represent whatthe key is going to be.
    KeyValueMapper<1st topic key, 2nd topic key, o/p key>

    // joinKtables
    1. either side of the data is going to trigger a join.
    2. So irrespective of whether the data is going to be sent to this any particular K table there will be a join trigger
     */

    /*
    // Joins Internal topics
    1. e join result it get stored in an internal topic.
    2. The internal topics names when you don't give store names

   joins-KSTREAM-OUTEROTHER-0000000007-store-changelog
joins-KSTREAM-OUTERSHARED-0000000006-store-changelog
joins-KSTREAM-OUTERTHIS-0000000006-store-changelog
joins-alphabets-store-changelog

The numbers added for creating of stores

* It is recommitted to give store names in the code always.
* So make sure to provide this value where you have control of the internal topics that gets created.

* After giving store names the internal topics will be
joins-alphabets-join-outer-other-join-store-changelog
joins-alphabets-join-outer-shared-join-store-changelog
joins-alphabets-join-outer-this-join-store-changelog
     */
}
