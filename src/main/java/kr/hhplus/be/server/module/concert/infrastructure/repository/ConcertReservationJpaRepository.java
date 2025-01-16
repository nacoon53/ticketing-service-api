package kr.hhplus.be.server.module.concert.infrastructure.repository;

import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservation, Long> {
    Optional<ConcertReservation> findBySeatId(long seatId);
    List<ConcertReservation> findByConcertId(long concertId);
}
