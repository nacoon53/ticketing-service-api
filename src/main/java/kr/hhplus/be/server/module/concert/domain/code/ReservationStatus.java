package kr.hhplus.be.server.module.concert.domain.code;

public enum ReservationStatus {
    RESERVED("예약 완료(결제 대기)"),
    PAID("결제 완료");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }
}
