package kr.hhplus.be.server.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class KafkaProducerTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    void testKafkaProducerAndConsumer() {
        String topic = "test-topic";
        String message = "Hello, Kafka!";
        kafkaProducer.sendMessage(topic, message);
    }

}