package kr.hhplus.be.server.payment.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.payment.domain.code.PaymentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    private long id;

    private String userId;
    private long reservationId;

    private double price;
    private LocalDateTime paymentDate;
    @Column(name="payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDateTime refundDate;
    private LocalDateTime refundRequestDate;

    @Builder
    public Payment(String userId, long reservationId, double price, LocalDateTime paymentDate, PaymentStatus status, LocalDateTime refundDate, LocalDateTime refundRequestDate) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.price = price;
        this.paymentDate = paymentDate;
        this.status = status;
        this.refundDate = refundDate;
        this.refundRequestDate = refundRequestDate;
    }


}
