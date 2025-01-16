package kr.hhplus.be.server.module.concert.infrastructure.repository;

import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {
    List<ConcertSeat> findByConcertIdAndStatus(long concertId, SeatStatus seatStatus);
}
