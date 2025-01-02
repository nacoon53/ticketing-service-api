package kr.hhplus.be.server.payment.controller;

import kr.hhplus.be.server.payment.dto.ConcertPaymentRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    // 결제 기능
    @PostMapping("/concert")
    public ResponseEntity<String> processPayment(@RequestBody ConcertPaymentRequestDTO request) {
        int status = 200;
        String msg = "요청이 정상적으로 처리되었습니다.";

        return ResponseEntity.status(status == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(msg);
    }
}