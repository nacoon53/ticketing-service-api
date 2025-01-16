package kr.hhplus.be.server.module.payment.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.hhplus.be.server.module.payment.application.usecase.PaymentUsecase;
import kr.hhplus.be.server.module.payment.presentation.dto.ConcertPaymentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name="결제 API", description = "결제 기능을 처리합니다.")
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentUsecase paymentUsecase;

    // 결제 기능
    @Operation(description = "콘서트의 결제를 처리합니다.")
    @PostMapping("/concert")
    public ResponseEntity<?> processPayment(@RequestBody ConcertPaymentRequestDTO dto, HttpServletRequest request) throws Exception {
        String userId = (String) request.getAttribute("loginId");
        String token = (String) request.getAttribute("token");

        paymentUsecase.payForConcert(userId, token, dto.reservationId());
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}