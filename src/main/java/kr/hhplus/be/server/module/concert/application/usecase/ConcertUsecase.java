package kr.hhplus.be.server.module.concert.application.usecase;

import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.module.concert.domain.entity.Concert;

import java.util.List;

public interface ConcertUsecase {
    //콘서트 목록 조회(날짜 포함)
    List<Concert> getConcertList();

    //예약 가능한 좌석 조회
    List<ConcertSeat> getAvailableSeat(long concertId);

    ConcertReservation reserveSeat(String userId, long seatId, String token) throws Exception;
}
