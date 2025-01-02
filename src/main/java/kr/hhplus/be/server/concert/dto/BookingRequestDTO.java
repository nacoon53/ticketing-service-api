package kr.hhplus.be.server.concert.dto;

import lombok.Builder;

@Builder
public class BookingRequestDTO {
    private long concertId;
    private String date;
    private long seatId;
}
