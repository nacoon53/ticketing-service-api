package kr.hhplus.be.server.module.concert.application.facade;

import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.module.concert.application.usecase.ConcertUsecase;
import kr.hhplus.be.server.module.concert.domain.code.ReservationStatus;
import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.module.concert.domain.entity.Concert;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertRepository;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertReservationRepository;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.test.base.BaseIntegretionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ConcertFacadeConcurrencyTest extends BaseIntegretionTest {
    @Autowired
    private ConcertUsecase concertUsecase;
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

    Concert givenConcert;

    String userId = "test";
    String token = "test:1";

    @BeforeEach
    void init() {
        Concert concert = Concert.builder()
                .concertName("씨엔블루 콘서트")
                .host("씨엔블루")
                .showDate(LocalDateTime.of(2025,10,7, 10,30,0))
                .build();

        givenConcert = concertRepository.save(concert);


        for(int i= 1; i<=10; i++) {
            ConcertSeat seat = ConcertSeat.builder()
                    .concertId(givenConcert.getId())
                    .number(i)
                    .status(SeatStatus.AVAILABLE)
                    .price(10_000)
                    .build();

            concertSeatRepository.save(seat);

            WaitListToken waitListToken = WaitListToken.builder()
                    .userId("user" + i)
                    .token("token" + i)
                    .status(TokenStatus.ACTIVE)
                    .build();
            waitListTokenRepository.save(waitListToken);
        }


    }

    @Test
    void 동시에_10명이_하나의_좌석을_예약한_경우_좌석은_한사람에게만_예약된다() throws Exception {
        //given
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long seatId = 1;

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalI = i+1;
            String userId = "user" + finalI;
            String token = "token" + finalI;
            executorService.execute(() -> {
                try {
                     concertUsecase.reserveSeat(userId, (long) seatId, token);
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
        ConcertReservation reservation = concertReservationRepository.findBySeatId(seatId).get();
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);

        //좌석 상태 값 확인
        Optional<ConcertSeat> seat = concertSeatRepository.findById(seatId);
        assertThat(seat).isNotNull();
        assertThat(seat.get().getConcertId()).isEqualTo(reservation.getConcertId());
        assertThat(seat.get().getStatus()).isEqualTo(SeatStatus.TEMP_ASSIGNED);

        //해당 유저의 토큰이 살아 있는지 확인
        WaitListToken token = waitListTokenRepository.findByUserId(reservation.getUserId());
        assertThat(token.isTokenActive()).isTrue();
    }

    @Test
    void 동시에_10명의_사용자가_각자_다른_좌석을_예매하면_모두_성공한다() throws Exception {
        //given
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalI = i+1;
            String tempUserId = "user" + finalI;
            String token = "token" + finalI;
            executorService.execute(() -> {
                try {
                    concertUsecase.reserveSeat(tempUserId, (long) finalI, token);
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

        List<ConcertReservation> reservations = concertReservationRepository.findByConcertId(givenConcert.getId());

        assertThat(reservations).isNotNull();
        assertThat(reservations.size()).isEqualTo(threadCount);

        //유저 별 예약 내용 확인
        for(ConcertReservation reservation : reservations) {
            String userId = reservation.getUserId();
            long seatId = reservation.getSeatId();

            //예약 상태 값 확인
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);

            //좌석 상태 값 확인
            Optional<ConcertSeat> seat = concertSeatRepository.findById(seatId);
            assertThat(seat).isNotNull();
            assertThat(seat.get().getConcertId()).isEqualTo(reservation.getConcertId());
            assertThat(seat.get().getStatus()).isEqualTo(SeatStatus.TEMP_ASSIGNED);
        }
    }
}