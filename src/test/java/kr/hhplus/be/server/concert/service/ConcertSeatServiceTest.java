package kr.hhplus.be.server.concert.service;

import kr.hhplus.be.server.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.concert.domain.repository.ConcertSeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConcertSeatServiceTest {
    @Mock
    ConcertSeatRepository concertSeatRepository;

    @InjectMocks
    ConcertSeatService concertSeatService;

    @Test
    void 다른_사람이_이미_예약한_좌석의_경우_에러를_리턴한다() {
        //given
        long seatId = 1;

        ConcertSeat givenSeat = ConcertSeat.builder()
                .concertId(1)
                .status(SeatStatus.TEMP_ASSIGNED)
                .build();

        given(concertSeatRepository.findById(anyLong())).willReturn(Optional.ofNullable(givenSeat));

        //when, then
        assertThatThrownBy(()->concertSeatService.chageStatusToTempAssigned(seatId, LocalDateTime.now()))
                .isInstanceOf(Exception.class)
                .hasMessage("이미 선점된 좌석입니다.");

        verify(concertSeatRepository, times(1)).findById(anyLong());
    }

    @Test
    void 다른_사람에게_임시_배정된_좌석의_경우_에러를_리턴한다() {
        //given
        long seatId = 1;

        ConcertSeat givenSeat = ConcertSeat.builder()
                .concertId(1)
                .status(SeatStatus.TEMP_ASSIGNED)
                .build();

        given(concertSeatRepository.findById(anyLong())).willReturn(Optional.ofNullable(givenSeat));

        //when, then
        assertThatThrownBy(()->concertSeatService.chageStatusToTempAssigned(seatId, LocalDateTime.now()))
                .isInstanceOf(Exception.class)
                .hasMessage("이미 선점된 좌석입니다.");

        verify(concertSeatRepository, times(1)).findById(anyLong());
    }

    @Test
    void 예약_가능한_좌석의_경우_좌석의_상태를_임시배정_값으로_변경한다() throws Exception {
        //given
        long seatId = 1;

        ConcertSeat givenSeat = ConcertSeat.builder()
                .concertId(1)
                .status(SeatStatus.AVAILABLE)
                .build();

        given(concertSeatRepository.findById(anyLong())).willReturn(Optional.ofNullable(givenSeat));
        given(concertSeatRepository.save(any(ConcertSeat.class)))
                .willAnswer(invocation -> invocation.getArgument(0));


        //when
        ConcertSeat seat = concertSeatService.chageStatusToTempAssigned(seatId, LocalDateTime.now());

        //then
        assertThat(seat.getStatus()).isEqualTo(SeatStatus.TEMP_ASSIGNED);

        verify(concertSeatRepository, times(1)).findById(anyLong());
        verify(concertSeatRepository, times(1)).save(any(ConcertSeat.class));
    }


    @Test
    void 좌석이_임시배정된_상태에서_결제완료로_변경에_성공한다() {
        // given
        long seatId = 1L;
        ConcertSeat seat = ConcertSeat.builder()
                .concertId(1L)
                .number(1)
                .status(SeatStatus.TEMP_ASSIGNED)
                .build();

        given(concertSeatRepository.findById(seatId)).willReturn(Optional.of(seat));
        given(concertSeatRepository.save(any(ConcertSeat.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        ConcertSeat updatedSeat = concertSeatService.changeStatusToPaid(seatId);

        // then
        assertThat(updatedSeat.getStatus()).isEqualTo(SeatStatus.PAID);
        assertThat(updatedSeat.getHoldExpiredAt()).isNull();

        verify(concertSeatRepository, times(1)).save(any(ConcertSeat.class));
    }

}