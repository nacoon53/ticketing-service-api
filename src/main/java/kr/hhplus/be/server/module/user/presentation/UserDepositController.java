package kr.hhplus.be.server.module.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.module.user.presentation.dto.ChargeRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="유저의 예치금 API", description = "유저의 예치금과 관련된 정보를 제공합니다.")
@RequestMapping("/api/v1/users/deposit")
public class UserDepositController {

    // 예치금 충전
    @Operation(description = "유저의 예치금을 충전합니다.")
    @PostMapping("/charge")
    public ResponseEntity<String> chargeBalance(@RequestBody ChargeRequestDTO request) {
        int status = 200;
        String msg = "요청이 정상적으로 처리되었습니다.";

        return ResponseEntity.status(status == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(msg);
    }

    // 예치금 조회
    @Operation(description = "유저의 예치금을 조회합니다.")
    @GetMapping
    public ResponseEntity<Double> getBalance() {
        double amount = 1000;
        return ResponseEntity.ok(amount);
    }
}