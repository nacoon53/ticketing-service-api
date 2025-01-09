package kr.hhplus.be.server.payment.domain.usecase;

public interface PaymentUsecase {
    void payForConcert(String token, long reservationId, String userId) throws Exception;
}
