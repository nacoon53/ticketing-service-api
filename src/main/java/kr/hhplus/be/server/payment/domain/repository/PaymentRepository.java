package kr.hhplus.be.server.payment.domain.repository;

import kr.hhplus.be.server.payment.domain.entity.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}
