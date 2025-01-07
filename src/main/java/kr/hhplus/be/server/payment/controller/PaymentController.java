package kr.hhplus.be.server.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.payment.dto.ConcertPaymentRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="결제 API", description = "결제 기능을 처리합니다.")
@RequestMapping("/api/v1/payment")
public class PaymentController {

    // 결제 기능
    @Operation(description = "콘서트의 결제를 처리합니다.")
    @PostMapping("/concert")
    public ResponseEntity<String> processPayment(@RequestBody ConcertPaymentRequestDTO request) {
        int status = 200;
        String msg = "요청이 정상적으로 처리되었습니다.";

        return ResponseEntity.status(status == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(msg);
    }
}