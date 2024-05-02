package com.kafka.greetingsstreamsspringboot.domain;

import java.time.LocalDateTime;

public record Greeting(String message, LocalDateTime timeStamp) {
}
