package kr.hhplus.be.server.concert.repository;

import kr.hhplus.be.server.concert.domain.entity.ConcertReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservation, Long> {
}
