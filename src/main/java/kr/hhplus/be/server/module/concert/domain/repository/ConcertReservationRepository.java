package kr.hhplus.be.server.module.concert.domain.repository;

import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;

import java.util.Optional;

public interface ConcertReservationRepository {
    ConcertReservation save(ConcertReservation allocation);

    Optional<ConcertReservation> findById(long reservationId);
}
