package kr.hhplus.be.server.concert.service.facade;

import kr.hhplus.be.server.auth.service.WaitListTokenService;
import kr.hhplus.be.server.concert.domain.entity.Concert;
import kr.hhplus.be.server.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.concert.domain.usecase.ConcertUsecase;
import kr.hhplus.be.server.concert.service.ConcertReservationService;
import kr.hhplus.be.server.concert.service.ConcertSeatService;
import kr.hhplus.be.server.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static kr.hhplus.be.server.ServerApplication.RESERVATION_HOLD_MINUTES;

@RequiredArgsConstructor
@Service
public class ConcertFacade implements ConcertUsecase {

    private final ConcertService concertService;
    private final ConcertSeatService concertSeatService;
    private final ConcertReservationService concertReservationService;
    private final WaitListTokenService waitListTokenService;

    @Override
    public List<Concert> getConcertList() {
        return concertService.getConcertList();
    }

    @Override
    public List<ConcertSeat> getAvailableSeat(long concertId) {
        return concertService.getAvailableSeat(concertId);
    }

    @Override
    public ConcertReservation reserveSeat(String userId, long seatId, String token) throws Exception{

        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(RESERVATION_HOLD_MINUTES);

        //좌석 테이블의 상태값과 선점 만료 시간 업데이트
        concertSeatService.chageStatusToTempAssigned(seatId, expiredAt);

        //토큰의 최종 갱신 시간 업데이트
        waitListTokenService.updateTokenTime(token);

        //예약 테이블에 row 추가
        return concertReservationService.reserveSeatByUser(seatId, userId, expiredAt);
    }
}
