package kr.hhplus.be.server.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WaitlistTokenIssueResponseDTO {
    private String token;
}
