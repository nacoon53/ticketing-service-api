package kr.hhplus.be.server.payment.dto;

import lombok.Builder;

@Builder
public class ConcertPaymentRequestDTO {
    private long concertId;
    private String date;
    private long seatId;
}
