package kr.hhplus.be.server.module.user.application.facade;

import kr.hhplus.be.server.module.user.application.usecase.UserDepositUsecase;
import kr.hhplus.be.server.module.user.domain.service.UserDepositService;
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
