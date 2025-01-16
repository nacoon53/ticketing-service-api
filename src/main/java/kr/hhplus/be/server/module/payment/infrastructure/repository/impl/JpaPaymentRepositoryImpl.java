package kr.hhplus.be.server.module.payment.infrastructure.repository.impl;

import kr.hhplus.be.server.module.payment.domain.repository.PaymentRepository;
import kr.hhplus.be.server.module.payment.infrastructure.repository.PaymentJpaRepository;
import kr.hhplus.be.server.module.payment.domain.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JpaPaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Payment findByUserIdAndReservationId(String userId, long reservationId) {
        return paymentJpaRepository.findByUserIdAndReservationId(userId, reservationId) ;
    }
}
