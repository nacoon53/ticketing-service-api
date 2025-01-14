package kr.hhplus.be.server.module.concert.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AvailableSeatResponseDTO {
    private long seatId;
    private int seatNumber;
    private double price;
}
