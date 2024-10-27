package com.realnaut.spaceships.infrastructure.broker;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @KafkaListener(topics = "login-topic", groupId = "realnaut-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }

}
