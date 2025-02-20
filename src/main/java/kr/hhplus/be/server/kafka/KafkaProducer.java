package kr.hhplus.be.server.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        try {
            this.kafkaTemplate.send(topic, message);
            log.info("Message sent: {}", message);
        }catch(Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}