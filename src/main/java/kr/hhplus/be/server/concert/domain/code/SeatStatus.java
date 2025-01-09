package kr.hhplus.be.server.concert.domain.code;

public enum SeatStatus {
    AVAILABLE("예약 가능"),
    TEMP_ASSIGNED("임시 배정(예약중)"),
    PAID("결제 완료");

    private final String description;

    SeatStatus(String description) {
        this.description = description;
    }
}
