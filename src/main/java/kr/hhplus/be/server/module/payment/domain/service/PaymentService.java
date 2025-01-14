package kr.hhplus.be.server.module.payment.domain.service;

import kr.hhplus.be.server.module.payment.domain.repository.PaymentRepository;
import kr.hhplus.be.server.module.payment.domain.code.PaymentStatus;
import kr.hhplus.be.server.module.payment.domain.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public void recordPaidPayment(String userId, long reservationId, double price) {
        Payment payment = Payment.builder()
                .userId(userId)
                .reservationId(reservationId)
                .price(price)
                .paymentDate(LocalDateTime.now())
                .status(PaymentStatus.PAID)
                .build();

        paymentRepository.save(payment);
    }
}
