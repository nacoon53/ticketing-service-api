package kr.hhplus.be.server.module.outbox.domain.service;

import kr.hhplus.be.server.module.outbox.domain.entity.OutboxEvent;
import kr.hhplus.be.server.module.outbox.domain.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OutboxService {
    private final OutboxRepository outboxRepository;

    public void createOutboxForReservationFinish(long reservationId) {
        OutboxEvent event = OutboxEvent.builder()
                .aggregateId(reservationId)
                .aggregateType("ConcertReservation")
                .eventType("ReservationCreated")
                .status("WAIT")
                .payload(String.valueOf(reservationId))
                .build();
        outboxRepository.save(event);
    }

    public void delete(OutboxEvent event) {
        outboxRepository.delete(event);
    }

    public List<OutboxEvent> findByOrderByCreatedAtAsc() {
        return outboxRepository.findByOrderByCreatedAtAsc();
    }
}
