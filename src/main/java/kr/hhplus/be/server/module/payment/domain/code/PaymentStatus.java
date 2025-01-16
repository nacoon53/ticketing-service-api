package kr.hhplus.be.server.module.payment.domain.code;

public enum PaymentStatus {
    PAID("결제 완료"),
    REQUEST_REFUND("환불 신청"),
    SUCCESS_REFUND("환불 완료");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
