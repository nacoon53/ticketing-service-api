package kr.hhplus.be.server.user.service.facade;

import kr.hhplus.be.server.user.domain.usecase.UserDepositUsecase;
import kr.hhplus.be.server.user.service.UserDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDepositFacade implements UserDepositUsecase {
    private final UserDepositService userDepositService;

    @Override
    public void chargeDeposit(String userId, double amount) throws Exception {
        userDepositService.chargeDeposit(userId, amount);
    }

    @Override
    public double getDeposit(String userId) {
        return userDepositService.getDeposit(userId);
    }
}
