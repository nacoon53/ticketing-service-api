package kr.hhplus.be.server.module.outbox.domain.repository;

import kr.hhplus.be.server.module.outbox.domain.entity.OutboxEvent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository {
    OutboxEvent save(OutboxEvent event);
    List<OutboxEvent> findByOrderByCreatedAtAsc();
    void delete(OutboxEvent event);
}
