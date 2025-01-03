package kr.hhplus.be.server.user.controller;

import kr.hhplus.be.server.user.dto.ChargeRequestDTO;
import kr.hhplus.be.server.user.dto.PaymentHistoryResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/payment")
public class UserPaymentController {

    // 결제 내역 조회
    @GetMapping("/history")
    public ResponseEntity<List<PaymentHistoryResponseDTO>> getPaymentHistory() {
        PaymentHistoryResponseDTO history1 = PaymentHistoryResponseDTO.builder()
                .paymentId(1L)
                .concertId(101L)
                .seatId(10L)
                .concertName("Concert A")
                .concertDate("2025-01-01")
                .price(50.0)
                .payDate("2025-01-01 12:00")
                .paymentStatus("결제완료")
                .refundRequestDate(null)
                .refundDate(null)
                .build();

        PaymentHistoryResponseDTO history2 = PaymentHistoryResponseDTO.builder()
                .paymentId(2L)
                .concertId(102L)
                .seatId(20L)
                .concertName("Concert B")
                .concertDate("2025-02-01")
                .price(70.0)
                .payDate("2025-02-01 14:00")
                .paymentStatus("환불신청")
                .refundRequestDate("2025-02-02 09:00")
                .refundDate(null)
                .build();

        PaymentHistoryResponseDTO history3 = PaymentHistoryResponseDTO.builder()
                .paymentId(3L)
                .concertId(102L)
                .seatId(20L)
                .concertName("Concert B")
                .concertDate("2025-02-01")
                .price(70.0)
                .payDate("2025-02-01 14:00")
                .paymentStatus("환불완료")
                .refundRequestDate("2025-02-02 09:00")
                .refundDate("2025-02-02 09:05")
                .build();

        return ResponseEntity.ok(List.of(history1, history2, history3));
    }
}