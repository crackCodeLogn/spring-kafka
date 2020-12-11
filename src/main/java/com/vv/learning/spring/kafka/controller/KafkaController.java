package com.vv.learning.spring.kafka.controller;

import com.vv.learning.spring.kafka.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vivek
 * @since 11/12/20
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    @Qualifier("Kafka-Spring-Producer")
    private Producer producer;

    @GetMapping("/send/{topic}/{msg}")
    public void sendOnTopic1(@PathVariable String topic, @PathVariable String msg) {
        LOGGER.info("Logged in input controller the request for transmission of {} on '{}'", msg, topic);
        String produceResult = sendToProducer(topic, msg);
        LOGGER.info("Producer result: {}", produceResult);
    }

    /*@GetMapping("/send/topic2/{msg}")
    public void sendOnTopic2(@PathVariable String msg) {
        LOGGER.info("Logged in input controller the request for transmission of {}", msg);
        String produceResult = sendToProducer(topic, msg);
        LOGGER.info("Producer result: {}", produceResult);
    }*/

    private String sendToProducer(String topic, Object msg) {
        return producer.sendObjectOnTopic(topic, msg);
    }
}
