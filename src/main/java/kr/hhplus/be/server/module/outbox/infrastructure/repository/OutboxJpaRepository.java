package kr.hhplus.be.server.module.outbox.infrastructure.repository;

import kr.hhplus.be.server.module.outbox.domain.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxJpaRepository extends JpaRepository<OutboxEvent, Long>  {
    List<OutboxEvent> findByOrderByCreatedAtAsc();
    void delete(OutboxEvent event);
}
