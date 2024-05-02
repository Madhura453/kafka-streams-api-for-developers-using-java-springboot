package com.kafka.advancedstreams.producer;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.kafka.advancedstreams.producer.ProducerUtil.publishMessageSync;

@Slf4j
public class JoinsMockDataProducer {


    public static void main(String[] args) throws InterruptedException {

        // Data useful publish for all joins

        String ALPHABETS_TOPIC = "alphabets";

        String ALPHABETS_ABBREVIATIONS_TOPIC = "alphabets_abbreviations";

        var alphabetMap = Map.of(
                "A", "A is the first letter in English Alphabets.",
                "B", "B is the second letter in English Alphabets.",
                "C","C is the Third letter in English Alphabets."
                              ,"E", "E is the fifth letter in English Alphabets."
                , "F","F is the Six letter in English Alphabets.",
                "z","Z is the twenty six letter in English Alphabets."
                // changing value description
//                "A", "A is the FIRST letter in English Alphabets.",
//                "B", "B is the SECOND letter in English Alphabets.",

        );
        publishMessages(alphabetMap, ALPHABETS_TOPIC);

        var alphabetAbbrevationMap = Map.of(
                "A", "Apple",
                "B", "Bus"
                , "C", "Cat"

        );
        publishMessages(alphabetAbbrevationMap, ALPHABETS_ABBREVIATIONS_TOPIC);

        alphabetAbbrevationMap = Map.of(
                "A", "Ant",
                "B", "Birthday.",
                "D", "Dog",
                "K", "Knife",
                "M","Monkey",
                "T","Tiger"

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

}

