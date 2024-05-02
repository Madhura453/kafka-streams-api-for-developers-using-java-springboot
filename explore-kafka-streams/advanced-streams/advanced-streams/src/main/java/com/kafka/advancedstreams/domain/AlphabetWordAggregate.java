package com.kafka.advancedstreams.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public record AlphabetWordAggregate(String key, Set<String> valueSet, int runningCount) {

    public AlphabetWordAggregate() {
        this("", new HashSet<>(), 0);
        log.info("If you start the application first time then it will be empty record");
    }

    public AlphabetWordAggregate updateEvents(String key, String newValue) {
        log.info("Before the update : {} ", this);
        log.info("New Record : key : {} , value : {} : ", key, newValue);
        int newRunningCount = this.runningCount+1;
        valueSet.add(newValue);
        AlphabetWordAggregate aggregated = new AlphabetWordAggregate(key, valueSet, newRunningCount);
        log.info("aggregated : {}", aggregated);
        return aggregated;
        /*
        So any time we get a new event for a given key, this function is going to be invoked.
        here we're updating running count and value set for grouped key
         */
    }
}
/*
    Key is actual key
    valueSet = the different values that being read. to avoid duplication of values
    runningCount= running count is going to be the continuously updated values of any new event that we receive for this given key
     */