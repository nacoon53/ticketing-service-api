package kr.hhplus.be.server.module.concert.application.usecase;

import kr.hhplus.be.server.module.concert.presentation.dto.AvailableSeatResponseDTO;
import kr.hhplus.be.server.module.concert.presentation.dto.ConcertReservationResponseDTO;
import kr.hhplus.be.server.module.concert.presentation.dto.ConcertResponseDTO;

import java.util.List;

public interface ConcertUsecase {
    //콘서트 목록 조회(날짜 포함)
    List<ConcertResponseDTO> getConcertList();

    //예약 가능한 좌석 조회
    List<AvailableSeatResponseDTO> getAvailableSeat(long concertId);

    ConcertReservationResponseDTO reserveSeat(String userId, long seatId, String token) throws Exception;
}
