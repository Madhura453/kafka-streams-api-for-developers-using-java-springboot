package com.kafka.orderskafkastreamsapp.domain;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public record TotalRevenue(String locationId, Integer runnuingOrderCount, BigDecimal runningRevenue) {
    /*
   It will execute each and every record. This class is used to hold aggregator result
   locationId = key of event. aggregation happens based on location id key
   runnuingOrderCount= How many orders we received per location
   runningRevenue=revenue which we are going to continuously update for each and every event that we receive
  runningRevenue=total revenue generated for that location
    */
    public TotalRevenue() {
        this("", 0, BigDecimal.valueOf(0.0));
       // log.info("when initial it will executed");
    }

    public TotalRevenue updateRunningRevenue(String key, Order value) {
//        log.info("Invoking this method when we receive new event for given location id");
//        log.info("continuously update event as we receive new events.");
//        log.info("update the order count when we receive new event for given location id");
        int newOrderCount = this.runnuingOrderCount + 1;
     //   log.info(" Added to previous value and update the revenue ");
        BigDecimal newRevenue = this.runningRevenue.add(value.finalAmount());
        return new TotalRevenue(key, newOrderCount, newRevenue);
    }

}
