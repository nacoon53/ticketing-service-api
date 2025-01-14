package kr.hhplus.be.server.module.payment.domain.repository;

import kr.hhplus.be.server.module.payment.domain.entity.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}