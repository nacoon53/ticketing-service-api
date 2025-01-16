package kr.hhplus.be.server.module.auth.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WaitListTokenIssueResponseDTO {
    private String token;
}
