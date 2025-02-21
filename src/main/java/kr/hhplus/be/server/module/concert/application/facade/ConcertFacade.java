package kr.hhplus.be.server.module.concert.application.facade;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.module.auth.domain.service.WaitListTokenService;
import kr.hhplus.be.server.module.concert.application.usecase.ConcertUsecase;
import kr.hhplus.be.server.module.concert.domain.entity.Concert;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.module.concert.domain.service.ConcertService;
import kr.hhplus.be.server.module.concert.presentation.dto.AvailableSeatResponseDTO;
import kr.hhplus.be.server.module.concert.presentation.dto.ConcertReservationResponseDTO;
import kr.hhplus.be.server.module.concert.presentation.dto.ConcertResponseDTO;
import kr.hhplus.be.server.module.externalApi.dataplatform.service.SendToDataPlatformService;
import kr.hhplus.be.server.module.outbox.domain.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ConcertFacade implements ConcertUsecase {

    private final ConcertService concertService;
    private final WaitListTokenService waitListTokenService;
    private final SendToDataPlatformService sendToDataPlatformService;
    private final OutboxService outboxService;

    @Override
    public List<ConcertResponseDTO> getConcertList() {
        List<Concert> concerts = concertService.getConcertList();
        return concerts.stream()
                .map(ConcertResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public List<AvailableSeatResponseDTO> getAvailableSeat(long concertId) {
        List<ConcertSeat> seats = concertService.getAvailableSeat(concertId);
        return seats.stream()
                .map(AvailableSeatResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public ConcertReservationResponseDTO reserveSeat(String userId, long seatId, String token) throws Exception{

        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        //좌석 테이블의 상태값과 선점 만료 시간 업데이트
        ConcertSeat seat = concertService.changeStatusToTempAssigned(seatId, expiredAt);

        //토큰의 최종 갱신 시간 업데이트
        waitListTokenService.updateTokenTime(token);

        //예약 테이블에 row 추가
        ConcertReservation reservation = concertService.reserveSeatByUser(seat.getConcertId(), seatId, userId, expiredAt);

        //좌석 예약 정보 데이터 플랫폼에 전달 -> 이벤트 전달을 위해 outbox 테이블에 이벤트 저장(kafka 이용)으로 코드 수정
        //sendToDataPlatformService.publishReservationEvent(userId, seatId); //seatId에 콘서트와 좌석 정보 모두 포함되어 있음
        outboxService.createOutboxForReservationFinish(reservation.getReservationId());

        return ConcertReservationResponseDTO.fromEntity(reservation);
    }
}
