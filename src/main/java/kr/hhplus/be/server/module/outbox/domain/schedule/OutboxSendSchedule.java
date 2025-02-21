package kr.hhplus.be.server.module.outbox.domain.schedule;

import kr.hhplus.be.server.module.outbox.domain.entity.OutboxEvent;
import kr.hhplus.be.server.module.outbox.domain.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OutboxSendSchedule {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxService outboxService;
    @Scheduled(fixedRate = 5000)
    public void sheduleKafkaEventSend() {
        List<OutboxEvent> events = outboxService.findByOrderByCreatedAtAsc();

        for(OutboxEvent event: events) {
            try {
                kafkaTemplate.send(event.getEventType(), event.getPayload());
                outboxService.delete(event);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
