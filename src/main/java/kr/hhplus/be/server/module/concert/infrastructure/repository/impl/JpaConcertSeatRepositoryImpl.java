package kr.hhplus.be.server.module.concert.infrastructure.repository.impl;

import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.module.concert.infrastructure.repository.ConcertSeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaConcertSeatRepositoryImpl implements ConcertSeatRepository {
    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    @Override
    public Optional<ConcertSeat> findById(long seatId) {
        return concertSeatJpaRepository.findById(seatId);
    }

    @Override
    public ConcertSeat save(ConcertSeat seat) {
        return concertSeatJpaRepository.save(seat);
    }

    @Override
    public List<ConcertSeat> findByConcertIdAndStatus(long concertId, SeatStatus seatStatus) {
        return concertSeatJpaRepository.findByConcertIdAndStatus(concertId, seatStatus);
    }
}
