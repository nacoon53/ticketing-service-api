package kr.hhplus.be.server.module.payment.infrastructure.repository;

import kr.hhplus.be.server.module.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
