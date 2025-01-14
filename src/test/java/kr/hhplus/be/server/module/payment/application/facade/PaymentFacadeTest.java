package kr.hhplus.be.server.module.payment.application.facade;

import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.module.concert.domain.code.ReservationStatus;
import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.module.concert.domain.entity.Concert;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertRepository;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertReservationRepository;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.module.user.domain.entity.User;
import kr.hhplus.be.server.module.user.domain.repository.UserRepository;
import kr.hhplus.be.server.test.base.BaseIntegretionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class PaymentFacadeTest extends BaseIntegretionTest {
    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertSeatRepository concertSeatRepository;

    @Autowired
    private ConcertReservationRepository concertReservationRepository;

    @Autowired
    private WaitListTokenRepository waitListTokenRepository;

    @Autowired
    private UserRepository userRepository;

    String token = "test:1";

    Concert givenConcert;
    ConcertSeat givenSeat;

    @BeforeEach
    void init() {
        User user = User.builder()
                .id("test")
                .deposit(10_000)
                .build();
        userRepository.save(user);

        WaitListToken waitListToken = WaitListToken.builder()
                .userId("test")
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

  /*  @AfterEach
    void tearDown() {
        concertRepository.clear();
        concertSeatRepository.clear();
        concertReservationRepository.clear();
        waitListTokenRepository.clear();
        userRepository.clear();
    }*/

    @Test
    void 좌석_예약이_되어있지_않다면_에러를_리턴한다() throws Exception {
        long reservationId = givenSeat.getId();
        String userId = "test";

        //when, then
        assertThatThrownBy(()-> paymentFacade.payForConcert(token, reservationId, userId))
                .isInstanceOf(Exception.class)
                .hasMessage("좌석 예약을 먼저 해주세요.");
    }


    @Test
    void 결제에_성공한다() throws Exception {
        //given
        long concertId = givenConcert.getId();
        long seatId = givenSeat.getId();
        String userId = "test";

        ConcertReservation givenReservation = ConcertReservation.builder()
                .concertId(concertId)
                .seatId(seatId)
                .userId(userId)
                .status(ReservationStatus.RESERVED)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .build();
        givenReservation = concertReservationRepository.save(givenReservation);
        long reservationId = givenReservation.getReservationId();


        //when
        paymentFacade.payForConcert(token, reservationId, userId);

        //then
        ConcertReservation reservation = concertReservationRepository.findById(reservationId).get();
        assertThat(reservation).isNotNull();
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.PAID);

        ConcertSeat seat = concertSeatRepository.findById(reservation.getSeatId()).get();
        assertThat(seat).isNotNull();
        assertThat(seat.getStatus()).isEqualTo(SeatStatus.PAID);

        User user = userRepository.findById(userId).get();

    }
}