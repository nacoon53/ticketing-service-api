package kr.hhplus.be.server.concert.service;

import kr.hhplus.be.server.concert.domain.code.ReservationStatus;
import kr.hhplus.be.server.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.concert.domain.repository.ConcertReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConcertReservationServiceTest {
    @Mock
    ConcertReservationRepository concertReservationRepository;

    @InjectMocks
    ConcertReservationService concertReservationService;

    @Test
    void 좌석_예약을_하면_Entity_객체가_만들어진다() {
        //given
        long seatId = 1;
        String userId = "test";

        given(concertReservationRepository.save(any(ConcertReservation.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        ConcertReservation allocation = concertReservationService.reserveSeatByUser(seatId, userId, null);

        //then
        assertThat(allocation.getSeatId()).isEqualTo(seatId);
        assertThat(allocation.getUserId()).isEqualTo(userId);

        verify(concertReservationRepository, times(1)).save(any(ConcertReservation.class));
    }

    @Test
    void 예약이_유효하면_true를_리턴한다() {
        // given
        long reservationId = 1L;
        ConcertReservation reservation = ConcertReservation.builder()
                .seatId(1L)
                .userId("testUser")
                .status(ReservationStatus.RESERVED)
                .expiredAt(LocalDateTime.now().plusMinutes(30))
                .build();

        given(concertReservationRepository.findById(reservationId)).willReturn(java.util.Optional.of(reservation));

        // when
        boolean isValid = concertReservationService.isValidReservation(reservationId);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    void 예약이_없으면_false를_리턴한다() {
        // given
        long reservationId = 1L;
        given(concertReservationRepository.findById(reservationId)).willReturn(java.util.Optional.empty());

        // when
        boolean isValid = concertReservationService.isValidReservation(reservationId);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void 만료된_예약이면_false를_리턴한다() {
        // given
        long reservationId = 1L;
        ConcertReservation expiredReservation = ConcertReservation.builder()
                .seatId(1L)
                .userId("testUser")
                .status(ReservationStatus.RESERVED)
                .expiredAt(LocalDateTime.now().minusMinutes(1)) // 만료된 예약
                .build();

        given(concertReservationRepository.findById(reservationId)).willReturn(java.util.Optional.of(expiredReservation));

        // when
        boolean isValid = concertReservationService.isValidReservation(reservationId);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void 예약_상태를_결제완료로_변경한다() {
        // given
        long reservationId = 1L;
        ConcertReservation reservation = ConcertReservation.builder()
                .seatId(1L)
                .userId("testUser")
                .status(ReservationStatus.RESERVED)
                .expiredAt(LocalDateTime.now().plusMinutes(30))
                .build();

        given(concertReservationRepository.findById(reservationId)).willReturn(java.util.Optional.of(reservation));
        given(concertReservationRepository.save(reservation)).willReturn(reservation);

        // when
        ConcertReservation updatedReservation = concertReservationService.changeStatusToPaid(reservationId);

        // then
        assertThat(updatedReservation.getStatus()).isEqualTo(ReservationStatus.PAID);
        verify(concertReservationRepository, times(1)).save(reservation);
    }
}