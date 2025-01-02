package kr.hhplus.be.server.user.controller;

import kr.hhplus.be.server.user.dto.ChargeRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/deposit")
public class UserDepositController {

    // 예치금 충전
    @PostMapping("/charge")
    public ResponseEntity<String> chargeBalance(@RequestBody ChargeRequestDTO request) {
        int status = 200;
        String msg = "요청이 정상적으로 처리되었습니다.";

        return ResponseEntity.status(status == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(msg);
    }

    // 예치금 조회
    @GetMapping
    public ResponseEntity<Double> getBalance() {
        double amount = 1000;
        return ResponseEntity.ok(amount);
    }
}