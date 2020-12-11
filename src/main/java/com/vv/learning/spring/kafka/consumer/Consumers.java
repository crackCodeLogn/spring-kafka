package com.vv.learning.spring.kafka.consumer;

import com.vv.learning.spring.kafka.core.Foo2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

/**
 * @author Vivek
 * @since 11/12/20
 */
@Component
public class Consumers {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumers.class);

    /*
     * Boot will autowire this into the container factory.
     */
    @Bean
    public SeekToCurrentErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
        return new SeekToCurrentErrorHandler(
                new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2));
    }

    @Bean
    public RecordMessageConverter converter() {
        return new StringJsonMessageConverter();
    }

    @KafkaListener(id = "${kafka.consumer.groups.primary}", topics = "${kafka.topics.primary}")
    public void listen(Foo2 foo, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        LOGGER.info("Received: {} on topic {}", foo, topic);
        if (foo.getFoo().startsWith("fail")) {
            throw new RuntimeException("failed");
        }
        //this.exec.execute(() -> System.out.println("Hit Enter to terminate..."));
    }

    @KafkaListener(id = "${kafka.consumer.groups.secondary}", topics = "${kafka.topics.secondary}")
    public void dltListen(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        LOGGER.info("Received from {}: {}", topic, in);
        //this.exec.execute(() -> System.out.println("Hit Enter to terminate..."));
    }
}
