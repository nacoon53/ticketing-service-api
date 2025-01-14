package kr.hhplus.be.server.module.payment.application.usecase;

public interface PaymentUsecase {
    void payForConcert(String token, long reservationId, String userId) throws Exception;
}
