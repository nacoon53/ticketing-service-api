package kr.hhplus.be.server.module.concert.presentation.dto;

import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AvailableSeatResponseDTO {
    private long seatId;
    private int seatNumber;
    private double price;

    public static AvailableSeatResponseDTO fromEntity(ConcertSeat seat) {
        return AvailableSeatResponseDTO.builder()
                .seatId(seat.getId())
                .seatNumber(seat.getNumber())
                .price(seat.getPrice())
                .build();
    }

}
