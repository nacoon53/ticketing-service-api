package kr.hhplus.be.server.payment.service;

import kr.hhplus.be.server.payment.domain.code.PaymentStatus;
import kr.hhplus.be.server.payment.domain.entity.Payment;
import kr.hhplus.be.server.payment.domain.repository.PaymentRepository;
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
