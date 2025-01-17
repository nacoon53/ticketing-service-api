package kr.hhplus.be.server.module.concert.domain.service;

import kr.hhplus.be.server.module.common.error.code.ErrorCode;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import kr.hhplus.be.server.module.concert.domain.code.ReservationStatus;
import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertReservationRepository;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertSeatRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {
    @Mock
    ConcertReservationRepository concertReservationRepository;

    @Mock
    ConcertSeatRepository concertSeatRepository;

    @InjectMocks
    ConcertService concertService;

    @Test
    void 다른_사람이_이미_예약한_좌석의_경우_에러를_리턴한다() {
        //given
        long seatId = 1;

        ConcertSeat givenSeat = ConcertSeat.builder()
                .concertId(1)
                .status(SeatStatus.TEMP_ASSIGNED)
                .build();

        BDDMockito.given(concertSeatRepository.findByIdWithLock(anyLong())).willReturn(Optional.ofNullable(givenSeat));

        //when, then
        Assertions.assertThatThrownBy(()->concertService.changeStatusToTempAssigned(seatId, LocalDateTime.now()))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.ALREADY_OCCUPIED_SEAT.getMessage());

        Mockito.verify(concertSeatRepository, Mockito.times(1)).findByIdWithLock(anyLong());
    }

    @Test
    void 다른_사람에게_임시_배정된_좌석의_경우_에러를_리턴한다() {
        //given
        long seatId = 1;

        ConcertSeat givenSeat = ConcertSeat.builder()
                .concertId(1)
                .status(SeatStatus.TEMP_ASSIGNED)
                .build();

        BDDMockito.given(concertSeatRepository.findByIdWithLock(anyLong())).willReturn(Optional.ofNullable(givenSeat));

        //when, then
        Assertions.assertThatThrownBy(()->concertService.changeStatusToTempAssigned(seatId, LocalDateTime.now()))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.ALREADY_OCCUPIED_SEAT.getMessage());

        Mockito.verify(concertSeatRepository, Mockito.times(1)).findByIdWithLock(anyLong());
    }

    @Test
    void 예약_가능한_좌석의_경우_좌석의_상태를_임시배정_값으로_변경한다() throws Exception {
        //given
        long seatId = 1;

        ConcertSeat givenSeat = ConcertSeat.builder()
                .concertId(1)
                .status(SeatStatus.AVAILABLE)
                .build();

        BDDMockito.given(concertSeatRepository.findByIdWithLock(anyLong())).willReturn(Optional.ofNullable(givenSeat));
        BDDMockito.given(concertSeatRepository.save(ArgumentMatchers.any(ConcertSeat.class)))
                .willAnswer(invocation -> invocation.getArgument(0));


        //when
        ConcertSeat seat = concertService.changeStatusToTempAssigned(seatId, LocalDateTime.now());

        //then
        Assertions.assertThat(seat.getStatus()).isEqualTo(SeatStatus.TEMP_ASSIGNED);
    }


    @Test
    void 좌석이_임시배정된_상태에서_결제완료로_변경에_성공한다() throws Exception {
        // given
        long seatId = 1L;
        ConcertSeat seat = ConcertSeat.builder()
                .concertId(1L)
                .number(1)
                .status(SeatStatus.TEMP_ASSIGNED)
                .build();

        BDDMockito.given(concertSeatRepository.findByIdWithLock(seatId)).willReturn(Optional.of(seat));
        BDDMockito.given(concertSeatRepository.save(ArgumentMatchers.any(ConcertSeat.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        ConcertSeat updatedSeat = concertService.changeStatusToPaidForSeat(seatId);

        // then
        Assertions.assertThat(updatedSeat.getStatus()).isEqualTo(SeatStatus.PAID);
        Assertions.assertThat(updatedSeat.getHoldExpiredAt()).isNull();

        Mockito.verify(concertSeatRepository, Mockito.times(1)).save(ArgumentMatchers.any(ConcertSeat.class));
    }
    @Test
    void 좌석_예약을_하면_Entity_객체가_만들어진다() {
        //given
        long seatId = 1;
        String userId = "test";

        BDDMockito.given(concertReservationRepository.save(ArgumentMatchers.any(ConcertReservation.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        ConcertReservation allocation = concertService.reserveSeatByUser(1, seatId, userId, null);

        //then
        Assertions.assertThat(allocation.getSeatId()).isEqualTo(seatId);
        Assertions.assertThat(allocation.getUserId()).isEqualTo(userId);

        Mockito.verify(concertReservationRepository, Mockito.times(1)).save(ArgumentMatchers.any(ConcertReservation.class));
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

        BDDMockito.given(concertReservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));

        // when
        boolean isValid = concertService.isValidReservation(reservationId);

        // then
        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    void 예약이_없으면_false를_리턴한다() {
        // given
        long reservationId = 1L;
        BDDMockito.given(concertReservationRepository.findById(reservationId)).willReturn(Optional.empty());

        // when
        boolean isValid = concertService.isValidReservation(reservationId);

        // then
        Assertions.assertThat(isValid).isFalse();
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

        BDDMockito.given(concertReservationRepository.findById(reservationId)).willReturn(Optional.of(expiredReservation));

        // when
        boolean isValid = concertService.isValidReservation(reservationId);

        // then
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    void 예약_상태를_결제완료로_변경한다() throws Exception {
        // given
        long reservationId = 1L;
        ConcertReservation reservation = ConcertReservation.builder()
                .seatId(1L)
                .userId("testUser")
                .status(ReservationStatus.RESERVED)
                .expiredAt(LocalDateTime.now().plusMinutes(30))
                .build();

        BDDMockito.given(concertReservationRepository.findByIdWithLock(reservationId)).willReturn(Optional.of(reservation));
        BDDMockito.given(concertReservationRepository.save(reservation)).willReturn(reservation);

        // when
        ConcertReservation updatedReservation = concertService.changeStatusToPaidForReservation(reservationId);

        // then
        Assertions.assertThat(updatedReservation.getStatus()).isEqualTo(ReservationStatus.PAID);
        Mockito.verify(concertReservationRepository, Mockito.times(1)).save(reservation);
    }
}