package com.kafka.greetingstreams.producer;

import lombok.extern.slf4j.Slf4j;

import static com.kafka.greetingstreams.producer.ProducerUtil.publishMessageSync;

@Slf4j
public class WordsProducerKTable {

    public static void main(String[] args) throws InterruptedException {

        //String key = null;

        var key = "A";

        var word = "PApple";
        var word1 = "PWAlligator";
        var word2 = "PWAmbulance";

       String WORDS_Topic = "words";

        var recordMetaData = publishMessageSync(WORDS_Topic, key,word);
        log.info("Published the alphabet message : {} ", recordMetaData);

        var recordMetaData1 = publishMessageSync(WORDS_Topic, key,word1);
        log.info("Published the alphabet message : {} ", recordMetaData1);

        var recordMetaData2 = publishMessageSync(WORDS_Topic, key,word2);
        log.info("Published the alphabet message : {} ", recordMetaData2);

        var bKey = "B";

        var bWord1 = "DBus";
        var bWord2 = "DBaby";
        var recordMetaData3 = publishMessageSync(WORDS_Topic, bKey,bWord1);
        log.info("Published the alphabet message : {} ", recordMetaData2);

        var recordMetaData4 = publishMessageSync(WORDS_Topic, bKey,bWord2);
        log.info("Published the alphabet message : {} ", recordMetaData2);

    }


}
