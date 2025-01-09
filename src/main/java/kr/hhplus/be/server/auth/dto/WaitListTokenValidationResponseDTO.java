package kr.hhplus.be.server.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WaitListTokenValidationResponseDTO {
    private boolean isValid;
    private boolean canAccess;
    private long waitNumber; // 대기 순서
}
