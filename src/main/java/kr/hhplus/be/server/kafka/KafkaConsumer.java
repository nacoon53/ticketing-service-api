package kr.hhplus.be.server.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

//    @Bean
//    public NewTopic topic() {
//        return TopicBuilder.name("test-topic")
//                .partitions(10)
//                .replicas(1)
//                .build();
//    }

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(Object record) {
        System.out.println(record);
        //System.out.println("Received message: " + record.value());
    }
}