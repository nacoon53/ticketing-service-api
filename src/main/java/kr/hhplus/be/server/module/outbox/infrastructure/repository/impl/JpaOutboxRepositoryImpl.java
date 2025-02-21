package kr.hhplus.be.server.module.outbox.infrastructure.repository.impl;

import kr.hhplus.be.server.module.outbox.domain.entity.OutboxEvent;
import kr.hhplus.be.server.module.outbox.domain.repository.OutboxRepository;
import kr.hhplus.be.server.module.outbox.infrastructure.repository.OutboxJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JpaOutboxRepositoryImpl implements OutboxRepository {
    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public OutboxEvent save(OutboxEvent event) {
        return outboxJpaRepository.save(event);
    }

    @Override
    public List<OutboxEvent> findByOrderByCreatedAtAsc() {
        return outboxJpaRepository.findByOrderByCreatedAtAsc();
    }

    @Override
    public void delete(OutboxEvent event) {

    }
}
