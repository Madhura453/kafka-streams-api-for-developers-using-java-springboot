package com.kafka.orderskafkastreamsapp.util;

import com.kafka.orderskafkastreamsapp.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
public class OrderTimeStampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        //log.info("ExtractRecordMetadataTimestamp implements TimestampExtractor. So here using same");
        Order order = (Order) record.value();
        if (order != null && order.orderedDateTime() != null) {
            LocalDateTime timeStamp = order.orderedDateTime();
            log.info("TimeStamp in extractor : {} ", timeStamp);
            return convertToInstantUTCFromIST(timeStamp);
        }
        //fallback to stream time
        return partitionTime;
    }

    private long convertToInstantUTCFromIST(LocalDateTime timeStamp) {
         /*
        in producer directly produce record with our system timestamp.
        here converting to instant GMT time
         */
        long instant=timeStamp.toInstant(ZoneOffset.ofHoursMinutes(-5,-30)).toEpochMilli();
        log.info("instant in extractor : {} ", instant);
        return instant;
    }
    /*
    1. record is order
    2. order contains "orderedDateTime". which holds record timestamp
    3. orderedDateTime to instant(long)
     */
}
