package kr.hhplus.be.server.module.user.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentHistoryResponseDTO {
    private long paymentId;
    private long concertId;
    private long seatId;
    private String concertName;
    private String concertDate;
    private double price;
    private String payDate;
    private String paymentStatus;
    private String refundRequestDate;
    private String refundDate;
}
