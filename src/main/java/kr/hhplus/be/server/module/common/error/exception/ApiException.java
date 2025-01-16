package kr.hhplus.be.server.module.common.error.exception;

import kr.hhplus.be.server.module.common.error.code.ErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return this.errorCode.getMessage();
    }
}
