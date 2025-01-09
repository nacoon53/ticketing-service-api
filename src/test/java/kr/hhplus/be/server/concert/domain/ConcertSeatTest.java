package kr.hhplus.be.server.concert.domain;

import kr.hhplus.be.server.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.concert.domain.code.SeatStatus;
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
    void 좌석이_AVAILABLE상태가_아닐때_예약이_불가능하다() {
        //given
        ConcertSeat tempAssignedSeat = ConcertSeat.builder()
                .status(SeatStatus.TEMP_ASSIGNED)
                .build();

        ConcertSeat reservedSeat = ConcertSeat.builder()
                .status(SeatStatus.PAID)
                .build();

        //then
        assertFalse(tempAssignedSeat.isSeatAvailable());
        assertTrue(tempAssignedSeat.isSeatOccupied());

        assertFalse(reservedSeat.isSeatAvailable());
        assertTrue(reservedSeat.isSeatOccupied());
    }
}