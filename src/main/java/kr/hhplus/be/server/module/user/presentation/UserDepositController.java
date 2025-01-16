package kr.hhplus.be.server.module.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.hhplus.be.server.module.user.application.usecase.UserDepositUsecase;
import kr.hhplus.be.server.module.user.presentation.dto.ChargeRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name="유저의 예치금 API", description = "유저의 예치금과 관련된 정보를 제공합니다.")
@RequestMapping("/api/v1/users/deposit")
public class UserDepositController {

    private final UserDepositUsecase userDepositUsecase;

    // 예치금 충전
    @Operation(description = "유저의 예치금을 충전합니다.")
    @PostMapping("/charge")
    public ResponseEntity<String> chargeBalance(@RequestBody ChargeRequestDTO dto, HttpServletRequest request) throws Exception {
        String userId = (String) request.getAttribute("loginId");

        userDepositUsecase.chargeDeposit(userId, dto.amount());

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    // 예치금 조회
    @Operation(description = "유저의 예치금을 조회합니다.")
    @GetMapping
    public ResponseEntity<Double> getBalance(HttpServletRequest request) {
        String userId = (String) request.getAttribute("loginId");

        double amount = userDepositUsecase.getDeposit(userId);

        return ResponseEntity.status(HttpStatus.OK).body(amount);
    }
}