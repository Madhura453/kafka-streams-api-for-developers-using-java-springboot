package com.kafka.orderskafkastreamsapp.domain;

public record Store(String locationId,
                    Address address,
                    String contactNum) {
}
