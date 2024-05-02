package com.kafka.advancedstreams.producer;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.kafka.advancedstreams.producer.ProducerUtil.publishMessageSync;
import static com.kafka.advancedstreams.producer.ProducerUtil.publishMessageSyncWithDelay;

@Slf4j
public class JoinKStreamWithKStreamMockDataProducer {

    public static void main(String[] args) throws InterruptedException {

        String ALPHABETS_TOPIC = "alphabets";

        String ALPHABETS_ABBREVIATIONS_TOPIC = "alphabets_abbreviations";

        var alphabetMap = Map.of(
                "A", "A is the first letter in English Alphabets.",
                "B", "B is the second letter in English Alphabets."
                //              ,"E", "E is the fifth letter in English Alphabets."
                //  ,
                // changing value description
//                "A", "A is the FIRST letter in English Alphabets.",
//                "B", "B is the SECOND letter in English Alphabets."
        );
       // publishMessages(alphabetMap, ALPHABETS_TOPIC);
        //JoinWindows
        //-4 & 4 will trigger the join
        //-6 -5 & 5, 6 wont trigger the join
        publishMessagesWithDelay(alphabetMap, ALPHABETS_TOPIC, 5);

        var alphabetAbbrevationMap = Map.of(
                "A", "Apple",
                "B", "Bus"
                , "C", "Cat"

        );
        publishMessages(alphabetAbbrevationMap, ALPHABETS_ABBREVIATIONS_TOPIC);

        alphabetAbbrevationMap = Map.of(
                "A", "Ant",
                "B", "Birthday."

        );
         publishMessages(alphabetAbbrevationMap, ALPHABETS_ABBREVIATIONS_TOPIC);

    }

    private static void publishMessages(Map<String, String> alphabetMap, String topic) {

        alphabetMap
                .forEach((key, value) -> {
                    var recordMetaData = publishMessageSync(topic, key, value);
                    log.info("Published the alphabet message : {} ", recordMetaData);
                });
    }

    private static void publishMessagesWithDelay(Map<String, String> alphabetMap, String topic, int delaySeconds) {
        alphabetMap
                .forEach((key, value) -> {
                    var recordMetaData = publishMessageSyncWithDelay(topic, key, value, delaySeconds);
                    log.info("Published the alphabet message : {} ", recordMetaData);
                });
    }

}
