package com.kafka.advancedstreams.producer;

import lombok.extern.slf4j.Slf4j;

import static com.kafka.advancedstreams.producer.ProducerUtil.publishMessageSync;
import static java.lang.Thread.sleep;

@Slf4j
public class WindowsMockDataProducer {

    public static final String WINDOW_WORDS_TOPIC = "windows-words";

    public static void main(String[] args) throws InterruptedException {

       // bulkMockDataProducer();
        bulkMockDataProducer_SlidingWindows();

    }

    private static void bulkMockDataProducer() throws InterruptedException {
        log.info("print the 1000 messages with delay 1000 ms i.e 1 s");
        var key = "A";
        var word = "Apple";
        int count = 0;
        while(count<100){
            var recordMetaData = publishMessageSync(WINDOW_WORDS_TOPIC, key,word);
            log.info("Published the alphabet message : {} ", recordMetaData);
            sleep(1000);
            count++;
        }
    }

    private static void bulkMockDataProducer_SlidingWindows() throws InterruptedException {
        log.info("our sliding window is going to do, it's going to create 10 sliding window buckets");
        var key = "A";
        var word = "Apple";
        int count = 0;
        while(count<10){
            var recordMetaData = publishMessageSync(WINDOW_WORDS_TOPIC, key,word);
            log.info("Published the alphabet message : {} ", recordMetaData);
            sleep(1000);
            count++;
        }
    }


}
