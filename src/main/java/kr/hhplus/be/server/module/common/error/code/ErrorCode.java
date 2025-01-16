package kr.hhplus.be.server.module.common.error.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //유저
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "U0001", "사용자를 찾을 수 없습니다."),

    //대기열 토큰
    ALREADY_EXISTS_WAITLIST_TOKEN(HttpStatus.CONFLICT, "W0001", "이미 발급받은 대기열 토큰이 존재 합니다."),
    NOT_FOUND_WAITLIST_TOKEN(HttpStatus.NOT_FOUND, "W0002", "토큰이 존재하지 않습니다. 새로 발급 받아 주세요."),
    NOT_YOUR_TURN(HttpStatus.FORBIDDEN, "W0003", "아직 순서가 되지 않았습니다. 조금만 더 기다려 주세요."),

    // 콘서트
    NOT_FOUND_SEAT(HttpStatus.NOT_FOUND, "C0001", "좌석 정보를 찾을 수 없습니다."),
    ALREADY_OCCUPIED_SEAT(HttpStatus.CONFLICT, "C0002", "이미 선점된 좌석입니다."),
    FAILED_CHANGE_STATUS_FOR_RESERVATION(HttpStatus.INTERNAL_SERVER_ERROR, "C0003", "예약 및 좌석의 상태값이 제대로 변경되지 않았습니다."),
    REQUIRE_RESERVATION(HttpStatus.BAD_REQUEST, "C0004", "좌석 예약을 먼저 해주세요."),

    // 유저 잔액
    OVER_MAXIMUM_DEPOSIT(HttpStatus.BAD_REQUEST, "D0001", "보유 금액이 최대 금액을 초과하였습니다."),
    BAD_REQUEST_FOR_DEPOSIT(HttpStatus.BAD_REQUEST, "D0002", "보유 금액이 유효하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}


