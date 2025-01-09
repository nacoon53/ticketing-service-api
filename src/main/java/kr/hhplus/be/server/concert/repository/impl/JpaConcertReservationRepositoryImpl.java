package kr.hhplus.be.server.concert.repository.impl;

import kr.hhplus.be.server.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.concert.domain.repository.ConcertReservationRepository;
import kr.hhplus.be.server.concert.repository.ConcertReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
