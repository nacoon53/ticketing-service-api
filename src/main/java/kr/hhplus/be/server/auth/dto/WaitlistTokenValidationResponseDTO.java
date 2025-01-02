package kr.hhplus.be.server.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WaitlistTokenValidationResponseDTO {
    private boolean isValid;
    private boolean canAccess;
    private int waitNumber; // 대기 순서
}
