package kr.hhplus.be.server.module.concert.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.module.concert.domain.code.ReservationStatus;
import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.module.concert.domain.entity.Concert;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertRepository;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertReservationRepository;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ConcertReservationRepository concertReservationRepository;

    public List<Concert> getConcertList() {
        return concertRepository.findByShowDateAfter(LocalDateTime.now());
    }

    public List<ConcertSeat> getAvailableSeat(long concertId) {
        return concertSeatRepository.findByConcertIdAndStatus(concertId, SeatStatus.AVAILABLE);

    }
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

    @Transactional
    public ConcertSeat chageStatusToTempAssigned(long seatId, LocalDateTime expiredAt) throws Exception{
        ConcertSeat seat = concertSeatRepository.findById(seatId).get();

        //좌석 유효성 검사
        if(seat.isSeatOccupied()) { //다른 사람이 예약했거나 이미 결제한 경우
            throw new Exception("이미 선점된 좌석입니다.");
        }

        // 좌석을 임시 배정으로 변경 && 예약 만료 시간 업데이트
        seat.changeStatusToTempAssigned(expiredAt);

        return concertSeatRepository.save(seat);
    }

    @Transactional
    public ConcertSeat changeStatusToPaid(long reservationId) throws Exception {

        try {
            // 예약 상태 변경
            ConcertReservation reservation = this.changeStatusToPaidForReservation(reservationId);
            // 좌석 상태 변경
            ConcertSeat seat = this.changeStatusToPaidForSeat(reservation.getSeatId());

            return seat;

        } catch (Exception e) {
            throw new Exception("예약 및 좌석의 상태값이 제대로 변경되지 않았습니다.");
        }

    }

    public ConcertSeat changeStatusToPaidForSeat(long seatId) throws Exception {
        // 좌석을 선점 상태로 변경
        ConcertSeat seat = concertSeatRepository.findById(seatId).get();
        seat.changeStatusToPaid();
        return concertSeatRepository.save(seat);

    }

    public ConcertReservation changeStatusToPaidForReservation(long reservationId) throws Exception {
        // 예약 상태 변경
        ConcertReservation reservation = concertReservationRepository.findById(reservationId).get();
        reservation.changeStatusToPaid();
        return concertReservationRepository.save(reservation);
    }
}
