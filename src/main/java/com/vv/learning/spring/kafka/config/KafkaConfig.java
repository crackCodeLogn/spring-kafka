package com.vv.learning.spring.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Vivek
 * @since 11/12/20
 */
@Configuration
public class KafkaConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${kafka.topics.primary}")
    public String primaryTopic;

    @Value("${kafka.topics.secondary}")
    public String secondaryTopic;

    @Value("${kafka.consumer.groups.primary}")
    public String consumerGroupPrimary;

    @Value("${kafka.consumer.groups.secondary}")
    public String consumerGroupSecondary;

    @Value("${kafka.producer.send.timeout:3000}")
    public int producerSendTimeout;

    @Value("${kafka.producer.send.threads:4}")
    public int producerSendThreads;

    @Bean
    public NewTopic topic1() {
        return new NewTopic(primaryTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic(secondaryTopic, 1, (short) 1);
    }

    @Bean
    @Qualifier("Producer-Executor")
    public ExecutorService producerExecutor() {
        return Executors.newFixedThreadPool(producerSendThreads);
    }
}
