package kr.hhplus.be.server.module.concert.infrastructure.repository;

import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservation, Long> {
}
