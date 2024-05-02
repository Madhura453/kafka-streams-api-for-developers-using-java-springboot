package com.kafka.greetingsstreamsspringboot.notes;

public class BasicNotes {
    /*
    @EnableKafkaStreams=
The biggest advantage here is that the spring framework takes care of managing the lifecycle of the
whole Kafka Streams application. This means we don't have to start up the Kafka Streams app manually and shutting it down manually.

official links

https://spring.io/projects/spring-kafka/

click on learn and
https://docs.spring.io/spring-kafka/docs/3.0.14-SNAPSHOT/reference/html/

https://docs.spring.io/spring-kafka/docs/3.0.14-SNAPSHOT/reference/html/#streams-kafka-streams

No need to write this

 KafkaStreams steams = new KafkaStreams(topology, properties);

        Runtime.getRuntime().addShutdownHook(new Thread(steams::close));

Spring manage for you.

https://docs.spring.io/spring-kafka/docs/3.0.14-SNAPSHOT/reference/html/#streams-kafka-streams

we are going to make this application to behave like a Kafka streams application=@EnableKafkaStreams
it contains KafkaStreamsDefaultConfiguration.class
= one which is going to set up this application as a Kafka Streams application.

  profiles:
    active: local= setting different profile. Look onto profile in spring

    spring:
  config:
    activate:
      on-profile: local

      = profile active for local

 public void process(StreamsBuilder streamsBuilder)=
Stream builder instance is something which is automatically created by the spring framework and it is
also injected into the spring context automatically for you

     */
}
