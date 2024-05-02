package com.kafka.greetingstreams.notes;

public class Notes {
    /*
    Topology represents whole processing logic kafka streams application
    KafkaStreams kafkaStreams = new KafkaStreams(topology,properties);
    this one executes the topology

    filter internally uses predicate

    Serdes
    If you are sure whole application using same serdes then use this
     properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);

   if you are not sure then internalize serdes in topology

   No.of tasks=No.of partitions
    Ideal value for properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG,"2");
    ideal values StreamsConfig.NUM_STREAM_THREADS_CONFIG=Runtime.getRuntime().availableProcessors()
    NUM_STREAM_THREADS =1. one thread executing 2 tasks
   New active tasks: [0_1, 0_0]
    New standby tasks: []
    Existing active tasks: []
    Existing standby tasks: []
    NUM_STREAM_THREADS will run the tasks
    if 2 threads 2 tasks
    1 thread=1 task, 2nd thread=2nd task
    NUM_STREAM_THREADS =2.
    New active tasks: [0_1]
    New standby tasks: []
    Existing active tasks: []
    Existing standby tasks: []
2024-01-12 17:29:25,040 [grretins-app-1c21ab27-d72b-4b0b-8145-f87076849777-StreamThread-1] INFO  o.a.k.s.p.internals.TaskManager - stream-thread [grretins-app-1c21ab27-d72b-4b0b-8145-f87076849777-StreamThread-1] Handle new assignment with:
    New active tasks: [0_0]
    New standby tasks: []
    Existing active tasks: []
    Existing standby tasks: []
     */
}
