package kr.hhplus.be.server.module.payment.application.usecase;

public interface PaymentUsecase {
    void payForConcert(String userId, String token, long reservationId) throws Exception;
}
