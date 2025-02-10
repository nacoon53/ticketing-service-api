package kr.hhplus.be.server.module.concert.infrastructure.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservation, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT cr FROM ConcertReservation cr WHERE cr.reservationId = :reservationId")
    Optional<ConcertReservation> findByIdWithLock(@Param("reservationId")long reservationId);
    Optional<ConcertReservation> findBySeatId(long seatId);
    List<ConcertReservation> findByConcertId(long concertId);
}
