package kr.hhplus.be.server.module.concert.application.facade;

import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.module.concert.domain.code.ReservationStatus;
import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.module.concert.domain.entity.Concert;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertRepository;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.test.base.BaseIntegretionTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@ActiveProfiles("test")
@SpringBootTest
class ConcertFacadeTest extends BaseIntegretionTest {
    @Autowired
    private ConcertFacade concertFacade;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertSeatRepository concertSeatRepository;

    @Autowired
    private WaitListTokenRepository waitListTokenRepository;

    Concert givenConcert;
    ConcertSeat givenSeat;

    String userId = "test";
    String token = "test:1";

    @BeforeEach
    void init() {
        WaitListToken waitListToken = WaitListToken.builder()
                .userId(userId)
                .token(token)
                .build();
        waitListTokenRepository.save(waitListToken);

        Concert concert = Concert.builder()
                .concertName("씨엔블루 콘서트")
                .host("씨엔블루")
                .showDate(LocalDateTime.of(2025,10,7, 10,30,0))
                .build();

        givenConcert = concertRepository.save(concert);

        ConcertSeat seat = ConcertSeat.builder()
                .concertId(givenConcert.getId())
                .number(1)
                .status(SeatStatus.AVAILABLE)
                .price(10_000)
                .build();

        givenSeat = concertSeatRepository.save(seat);
    }

    @Test
    void 예약에_성공한다() throws Exception {
        //then
        long concertId = givenConcert.getId();
        long seatId = givenSeat.getId();

        //when
        ConcertReservation reservation = concertFacade.reserveSeat(userId, seatId, token);

        //then
        Assertions.assertThat(reservation).isNotNull();
        Assertions.assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
    }

}