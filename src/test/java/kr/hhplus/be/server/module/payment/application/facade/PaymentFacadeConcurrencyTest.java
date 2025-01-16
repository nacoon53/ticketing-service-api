package kr.hhplus.be.server.module.payment.application.facade;

import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.module.auth.domain.service.WaitListTokenService;
import kr.hhplus.be.server.module.concert.application.facade.ConcertFacade;
import kr.hhplus.be.server.module.concert.application.usecase.ConcertUsecase;
import kr.hhplus.be.server.module.concert.domain.code.ReservationStatus;
import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.module.concert.domain.entity.Concert;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertRepository;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertReservationRepository;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.module.concert.domain.service.ConcertService;
import kr.hhplus.be.server.module.payment.application.usecase.PaymentUsecase;
import kr.hhplus.be.server.module.payment.domain.entity.Payment;
import kr.hhplus.be.server.module.payment.domain.repository.PaymentRepository;
import kr.hhplus.be.server.module.payment.domain.service.PaymentService;
import kr.hhplus.be.server.module.user.domain.entity.User;
import kr.hhplus.be.server.module.user.domain.repository.UserRepository;
import kr.hhplus.be.server.module.user.domain.service.UserDepositService;
import kr.hhplus.be.server.test.base.BaseIntegretionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class PaymentFacadeConcurrencyTest extends BaseIntegretionTest {
    @Autowired
    private ConcertFacade concertFacade;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private ConcertSeatRepository concertSeatRepository;
    @Autowired
    private ConcertReservationRepository concertReservationRepository;
    @Autowired
    private WaitListTokenRepository waitListTokenRepository;
    @Autowired
    private PaymentUsecase paymentUsecase;
    @Autowired
    private ConcertUsecase concertUsecase;
    @Autowired
    private ConcertService concertService;
    @Autowired
    private UserDepositService userDepositService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WaitListTokenService waitListTokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    Concert givenConcert;
    ConcertSeat givenSeat;
    User givenUser;
    WaitListToken givenToken;

    String userId = "test";
    String token = "test:1";

    @BeforeEach
    void init() {
        User user = User.builder()
                .id(userId)
                .deposit(50_000)
                .build();

        givenUser = userRepository.save(user);

        WaitListToken waitListToken = WaitListToken.builder()
                .userId(userId)
                .token(token)
                .build();
        givenToken = waitListTokenRepository.save(waitListToken);

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
    void 한_사람이_결제를_3번_호출해도_한번만_결제된다() throws Exception {
        //given
        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long reservationId = 1;

        concertUsecase.reserveSeat(userId, givenSeat.getId(), givenToken.getToken());

        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    paymentUsecase.payForConcert(userId, givenToken.getToken(), reservationId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();  // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        //then
        //예약 상태 값 확인
        Optional<ConcertReservation> reservation = concertReservationRepository.findById(reservationId);
        assertThat(reservation.get().getStatus()).isEqualTo(ReservationStatus.PAID);

        //좌석 상태 값 확인
        long seatId = reservation.get().getSeatId();
        Optional<ConcertSeat> seat = concertSeatRepository.findById(seatId);
        assertThat(seat).isNotNull();
        assertThat(seat.get().getConcertId()).isEqualTo(reservation.get().getConcertId());
        assertThat(seat.get().getStatus()).isEqualTo(SeatStatus.PAID);

        //유저 잔액 차감 확인
        User user = userRepository.findById(userId).get();
        assertThat(user).isNotNull();
        assertThat(user.getDeposit()).isEqualTo(givenUser.getDeposit() - givenSeat.getPrice());

        //결제 데이터 저장 됐는지 확인
        Payment payment = paymentRepository.findByUserIdAndReservationId(userId, reservation.get().getReservationId());
        assertThat(payment).isNotNull();

        //해당 유저의 토큰이 만료됐는지 확인
        WaitListToken token = waitListTokenRepository.findByUserId(reservation.get().getUserId());
        assertThat(token.isTokenActive()).isFalse();
    }

}