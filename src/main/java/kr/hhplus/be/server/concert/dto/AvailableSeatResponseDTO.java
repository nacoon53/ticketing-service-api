package kr.hhplus.be.server.concert.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AvailableSeatResponseDTO {
    private long seatId;
    private int seatNumber;
    private double price;
}
