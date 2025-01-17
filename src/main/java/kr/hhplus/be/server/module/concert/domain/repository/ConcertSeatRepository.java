package kr.hhplus.be.server.module.concert.domain.repository;

import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatRepository {
    Optional<ConcertSeat> findById(long seatId);
    Optional<ConcertSeat> findByIdWithLock(long seatId);
    ConcertSeat save(ConcertSeat seat);

    List<ConcertSeat> findByConcertIdAndStatus(long concertId, SeatStatus seatStatus);
}
