package kr.hhplus.be.server.module.concert.infrastructure.repository.impl;

import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertReservationRepository;
import kr.hhplus.be.server.module.concert.infrastructure.repository.ConcertReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaConcertReservationRepositoryImpl implements ConcertReservationRepository {
    private final ConcertReservationJpaRepository concertReservationJpaRepository;

    @Override
    public ConcertReservation save(ConcertReservation allocation) {
        return concertReservationJpaRepository.save(allocation);
    }

    @Override
    public Optional<ConcertReservation> findById(long reservationId) {
        return concertReservationJpaRepository.findById(reservationId);
    }

    @Override
    public Optional<ConcertReservation> findByIdWithLock(long reservationId) {
        return concertReservationJpaRepository.findByIdWithLock(reservationId);
    }

    @Override
    public Optional<ConcertReservation> findBySeatId(long seatId) {
        return concertReservationJpaRepository.findBySeatId(seatId);
    }

    @Override
    public List<ConcertReservation> findByConcertId(long concertId) {
        return concertReservationJpaRepository.findByConcertId(concertId);
    }
}
