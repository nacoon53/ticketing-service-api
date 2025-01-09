package kr.hhplus.be.server.concert.service;

import kr.hhplus.be.server.concert.domain.code.ReservationStatus;
import kr.hhplus.be.server.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.concert.domain.repository.ConcertReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConcertReservationService {
    private final ConcertReservationRepository concertReservationRepository;

    public ConcertReservation reserveSeatByUser(long seatId, String userId, LocalDateTime expiredAt) {
        ConcertReservation reservation = ConcertReservation.builder()
                .seatId(seatId)
                .userId(userId)
                .status(ReservationStatus.RESERVED)
                .expiredAt(expiredAt)
                .build();

        return concertReservationRepository.save(reservation);
    }

    public boolean isValidReservation(long reservationId) {
        Optional<ConcertReservation> reservationOptional = concertReservationRepository.findById(reservationId);

        // 예약이 없으면 false 반환
        if (reservationOptional.isEmpty()) {
            return false;
        }

        ConcertReservation reservation = reservationOptional.get();

        return reservation.isValidReservation(LocalDateTime.now());
    }

    public ConcertReservation changeStatusToPaid(long reservationId) {
        ConcertReservation reservation = concertReservationRepository.findById(reservationId).get();

        reservation.changeStatusToPaid();
        return concertReservationRepository.save(reservation);
    }
}
