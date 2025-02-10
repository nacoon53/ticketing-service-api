package kr.hhplus.be.server.module.concert.infrastructure.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {
    List<ConcertSeat> findByConcertIdAndStatus(long concertId, SeatStatus seatStatus);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT cs FROM ConcertSeat cs WHERE cs.id = :seatId")
    Optional<ConcertSeat> findByIdWithLock(@Param("seatId")long seatId);
}
