package com.kafka.advancedstreams.notes;

public class AggregationOperationsNotes {
     /*
    Aggregation
    1) Key for record is mandatory.
    2) After . cache.max.bytes.buffering
       •commit.interval.ms
     These two meets then aggregation will happened
     * aggregation process will continue if any new record will come
     * Grouped the data is first step for any StatefulOperation
     * aggregate-KSTREAM-AGGREGATE-STATE-STORE-0000000002-changelog == internal aggregate topic
        To maintain data.NextTime the record will update
     * when we restart the application it will store maintains previous records. based ont that it reconstructs
        The Topology. Updated the latest value.
      * If first app running A=2. then next time app  restated then record will be A=new value ex: A, 6
     */
    /*
    Group By
        The list of topics created for group by internally
        aggregate-KSTREAM-AGGREGATE-STATE-STORE-0000000003-changelog
        aggregate-KSTREAM-AGGREGATE-STATE-STORE-0000000003-repartition
       * we can change the key and  we can give custom key.
       * And if you'd like to change the key to a different value, then you can use a group operator.
       * To manage this internally it will create topics
       aggregate-KSTREAM-AGGREGATE-STATE-STORE-0000000003-changelog= for managing new key
       aggregate-KSTREAM-AGGREGATE-STATE-STORE-0000000003-repartition= for reparation the records
     */

    /*
      reduce
      * reduce multiple values into single value
      * The drawback is return type must and should is value type.
     */
    /*
     Aggregate
     It returns different data type other than value
     */
    /*
        materialized view
        if we don't provide materialized view
        * The Kafka Streams did behind the scenes is that it created an internal Kafka topic and stored
           the complete state over there for fault tolerance and retention. to store previous records and update
           current records . kafka internal topic stores the records. and if required it will do repartition.

        if we provide materialized view
        * we are creating our own store instead of Kafka streams, creating one for us
        * create a state store and then maintain the data in a internal Kafka topic.
        * if type doesn't match then definitely we need to provide  materialized
        * for count it is optional and for aggregate it is compulsory
        * But if you don't give it for aggregate operator, you might run into errors because the type that we are converting to from string
          is a completely different type.You might have to set up all those default deserializer serializer and all those other configuration
          in order to avoid all those things.We are explicitly setting that materialized views key and value type over here.
     */
}
