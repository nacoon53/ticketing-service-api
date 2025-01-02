package kr.hhplus.be.server.auth.controller;


import kr.hhplus.be.server.auth.dto.WaitlistTokenIssueResponseDTO;
import kr.hhplus.be.server.auth.dto.WaitlistTokenValidationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wait-token")
public class WaitlistTokenController {

    // 유저 대기열 토큰 발급
    @PostMapping("/issue")
    public ResponseEntity<WaitlistTokenIssueResponseDTO> issueWaitlistToken() {
        String token = "temp_token";
        WaitlistTokenIssueResponseDTO response = WaitlistTokenIssueResponseDTO.builder()
                .token(token)
                .build();
        return ResponseEntity.ok(response);
    }

    // 유저 대기열 토큰 검증
    @PostMapping("/validate")
    public ResponseEntity<WaitlistTokenValidationResponseDTO> validateWaitlistToken() {
        WaitlistTokenValidationResponseDTO response =  WaitlistTokenValidationResponseDTO.builder()
                .isValid(true)
                .canAccess(true)
                .waitNumber(1)
                .build();

       return ResponseEntity.ok(response);
    }
}
