package kr.hhplus.be.server.module.concert.infrastructure.repository;

import kr.hhplus.be.server.module.concert.domain.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
    List<Concert> findByShowDateAfter(LocalDateTime targetDateTime);
}
