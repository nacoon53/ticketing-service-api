package kr.hhplus.be.server.auth.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.auth.dto.WaitListTokenIssueResponseDTO;
import kr.hhplus.be.server.auth.dto.WaitListTokenValidationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="대기열토큰 API", description = "대기열 토큰과 관련된 정보를 제공합니다.")
@RequestMapping("/api/v1/wait-token")
public class WaitListTokenController {

    // 유저 대기열 토큰 발급
    @Operation(description = "유저의 대기열 토큰을 발급합니다.")
    @PostMapping("/issue")
    public ResponseEntity<WaitListTokenIssueResponseDTO> issueWaitlistToken() {
        String token = "temp_token";
        WaitListTokenIssueResponseDTO response = WaitListTokenIssueResponseDTO.builder()
                .token(token)
                .build();
        return ResponseEntity.ok(response);
    }

    // 유저 대기열 토큰 검증
    @Operation(description = "유저의 대기열 토큰을 검증합니다.")
    @PostMapping("/validate")
    public ResponseEntity<WaitListTokenValidationResponseDTO> validateWaitlistToken() {
        WaitListTokenValidationResponseDTO response =  WaitListTokenValidationResponseDTO.builder()
                .isValid(true)
                .canAccess(true)
                .waitNumber(1)
                .build();

       return ResponseEntity.ok(response);
    }
}