package kr.hhplus.be.server.module.concert.domain.entity;

import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ConcertSeatTest {
    @Test
    void 좌석이_AVAILABLE상태일때_예약가능하다() {
        //given
        ConcertSeat seat = ConcertSeat.builder()
                .status(SeatStatus.AVAILABLE)
                .build();

        //then
        assertTrue(seat.isSeatAvailable());
        assertFalse(seat.isSeatOccupied());
    }

    @Test
    void 좌석이_결제됐다면_예약이_불가능하다() {
        //given
        ConcertSeat reservedSeat = ConcertSeat.builder()
                .status(SeatStatus.PAID)
                .build();

        //then
        assertFalse(reservedSeat.isSeatAvailable());
        assertTrue(reservedSeat.isSeatOccupied());
    }
}