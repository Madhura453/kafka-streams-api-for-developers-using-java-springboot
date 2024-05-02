package com.kafka.greetingstreams.exploreOperators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.event.KeyValuePair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ExploreOperators {

    public static KStream<String, String> filterOperator(KStream<String, String> stream) {
        return stream.filter((readOnlyKey, value) -> value.length() > 3)
                .mapValues((readOnlyKey, value) -> value.toUpperCase());
    }

    public static KStream<String, String> filterNotOperator(KStream<String, String> stream) {
        return stream.filterNot((readOnlyKey, value) -> value.length() > 3)
                .mapValues((readOnlyKey, value) -> value.toUpperCase());
    }

    public static KStream<String, String> mapOperator(KStream<String, String> stream) {
        return stream.map((readOnlyKey, value) ->
                KeyValue.pair(StringUtils.isBlank(readOnlyKey) ? "" : readOnlyKey.toUpperCase(), value.toUpperCase()));
    }

    public static KStream<String, String> flatmapOperator(KStream<String, String> stream) {
       /*
       when single event is going to create multiple events downstream
       APPLe->5 events = a  p p l e
        */
        return stream.flatMap((key, value) -> {
            List<String> eventsList = Arrays.asList(value.split(""));
            return eventsList.stream().map(eventValue -> KeyValue.pair(key.toUpperCase(), eventValue.toUpperCase()))
                    .collect(Collectors.toList());
        });
    }

    public static KStream<String, String> flatmapValueOperator(KStream<String, String> stream) {
       /*
       when single event is going to create multiple events downstream
       APPLe->5 events = a  p p l e
        */
        return stream.flatMapValues((key, value) -> {
            List<String> eventsList = Arrays.asList(value.split(""));
            return eventsList.stream().map(String::toUpperCase)
                    .collect(Collectors.toList());
        });
    }

    public static KStream<String, String> peekOperator(KStream<String, String> stream) {
        // peek is used what's going on inside topology stream logic
        // peek is used for logging and no business logic
        return stream.filter((readOnlyKey, value) -> value.length() > 3)
                .peek((key,value)->log.info("after filter key: {}, value : {} ",key,value))
                .mapValues((readOnlyKey, value) -> value.toUpperCase())
                .peek((key,value)->log.info("after mapValues key: {}, value : {} ",key,value))
                .flatMapValues((key, value) -> {
                    List<String> eventsList = Arrays.asList(value.split(""));
                    return eventsList.stream().map(String::toUpperCase)
                            .collect(Collectors.toList());
                });
    }

}
