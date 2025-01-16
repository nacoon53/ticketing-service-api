package kr.hhplus.be.server.module.auth.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WaitListTokenValidationResponseDTO {
    private boolean isValid;
    private boolean canAccess;
    private long waitNumber; // 대기 순서
}
