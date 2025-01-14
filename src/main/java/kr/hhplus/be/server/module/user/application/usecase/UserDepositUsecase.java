package kr.hhplus.be.server.module.user.application.usecase;

public interface UserDepositUsecase {
    // 잔액 충전
    void chargeDeposit(String userId, double amount) throws Exception;

    // 잔액 조회
    double getDeposit(String userId);
}
