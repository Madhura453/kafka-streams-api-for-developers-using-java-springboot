package com.kafka.greetingstreams.notes;

public class KTableNotes {
    /*
    RocksDb is used to store previous key and value. if app crashes.
   the data is also stored in an internal change log topic for fault tolerance purposes.
   The data stores in internal change log topic
   kTable and global k table have less operators compare to kStream
     */

       /*
        Materialized.as = to get latest record
        buffering=Preloading data into a reserved area of memory
         streamsBuilder.table(WORDS_Topic= once the message is consumed by this table.
       table keeps buffering for a certain time frame. And then once the time frame is exhausted. Then
       it will do  it is going to take the latest value for that given key and
       then publish that message downstream. So in this case, this key table waits for a certain time frame and buffers
       the records within the time frame.And once the time frame is exhausted, it's going to be sending that message to the downstream.

      * It skips the records if key was null
      Skipping record due to null key. topic=[words] partition=[1] offset=[0]

      cache.max.bytes.buffering default size is 10MB
      * this cache holds a value in the memory.And once that memory is full, then it will decide to
      send the value to the downstream nodes.
      * commit.interval.ms = 30000 (30 seconds) default
      * Any time we publish the data, the data doesn't get emitted immediately to the downstream nodes.
        It actually waits for 30 seconds and then it sends the data to the downstream nodes.
       * Basically, there are two configurations that controls this.
    1) is cache dot max dot bites, dot buffering property.This also means k table uses a cache internally for
        de-duplication and the cache serves as a buffer
      2) the other property is the commit interval dot milliseconds.

      The storing buffer maintains internally by changelog topic= kTable-app-words-store-changelog
      * * So when you restart the application.is going to go to the Kafka topic kTable-app-words-store-changelog and then get all the values for this
state store that word store, and it's going to match this name with this Changelog topic.
So as you can see, K table word store, this is a name that we provided and then it adds this Changelog topic.
So it's going to read all these values for the given key and value and then it's going to get the updated
values for this key and value.
         */
    /*
    GlobalKtable
    id 2 applications running
    1) application id =1,= tasks t1,t2= instance 1(application id 1 instance)
   2) application id =2,= tasks t3,t4= instance 2 (application id 2 instance)
   kTable
    instance 1  have access to only keys of t1 and t2
    instance 2  have access to only keys of t3 and t4
   GlobalKTable
   instance 1  and instance 2 have access to all the keys in the topic
     */
}
