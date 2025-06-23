package com.zwash.booking.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.zwash.common.pojos.Car;


@Service
public class MessageListener {


    @KafkaListener(
            topics = "car-topic", // Replace with the actual topic name
            groupId = "${spring.kafka.consumer.group-id}"
        )
        public void listenCarMessages(Car car) {
            // Process the received Car message here
            // car is the deserialized Car object
        }
}
