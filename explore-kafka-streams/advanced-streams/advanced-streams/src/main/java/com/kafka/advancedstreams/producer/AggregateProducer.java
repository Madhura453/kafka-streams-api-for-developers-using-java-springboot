package com.kafka.advancedstreams.producer;

import lombok.extern.slf4j.Slf4j;

import static com.kafka.advancedstreams.producer.ProducerUtil.publishMessageSync;

@Slf4j
public class AggregateProducer {


    public static void main(String[] args) throws InterruptedException {

        String AGGREGATE_TOPIC = "aggregate";

        var key = "A";
        //String key = null;

        var word = "Apple";
        var word1 = "Alligator";
        var word2 = "Ambulance";

        var recordMetaData = publishMessageSync(AGGREGATE_TOPIC, key,word);
        log.info("Published the alphabet message : {} ", recordMetaData);

        var recordMetaData1 = publishMessageSync(AGGREGATE_TOPIC, key,word1);
        log.info("Published the alphabet message : {} ", recordMetaData1);

        var recordMetaData2 = publishMessageSync(AGGREGATE_TOPIC, key,word2);
        log.info("Published the alphabet message : {} ", recordMetaData2);

        var bKey = "B";
        //String bKey = null;

        var bWord1 = "Bus";
        var bWord2 = "Baby";
        var recordMetaData3 = publishMessageSync(AGGREGATE_TOPIC, bKey,bWord1);
        log.info("Published the alphabet message : {} ", recordMetaData2);

        var recordMetaData4 = publishMessageSync(AGGREGATE_TOPIC, bKey,bWord2);
        log.info("Published the alphabet message : {} ", recordMetaData2);

    }



}
