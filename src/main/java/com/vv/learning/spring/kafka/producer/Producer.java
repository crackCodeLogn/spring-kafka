package com.vv.learning.spring.kafka.producer;

import com.vv.learning.spring.kafka.config.KafkaConfig;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Vivek
 * @since 11/12/20
 */
@Component
@Qualifier("Kafka-Spring-Producer")
public class Producer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @Autowired
    private KafkaConfig kafkaConfig;

    public String sendObjectOnTopic(String topic, Object msg) {
        LOGGER.info("Received instruction to broadcast msg over kafka topic '{}': {}", topic, msg);
        String producerResult = "FAIL!";
        try {
            producerResult = kafkaConfig.producerExecutor()
                    .submit(createProducerTask(topic, msg))
                    .get();
            LOGGER.info("Obtained producer result: {}", producerResult);
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Failed to publish on producer. ", e);
        }
        return producerResult;
    }

    private Callable<String> createProducerTask(String topic, Object msg) {
        return () -> {
            try {
                SendResult<Object, Object> sendResult = template.send(topic, msg)
                        .get(kafkaConfig.producerSendTimeout, TimeUnit.MILLISECONDS);
                RecordMetadata recordMetadata = sendResult.getRecordMetadata();
                String result = String.format("%s %d %dB", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.serializedValueSize());
                LOGGER.info("Record meta data obtained from cluster: {}", result);
                return result;
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                LOGGER.error("Failed to transmit msg successfully. ", e);
            }
            return "FAILED!!";
        };
    }
}
