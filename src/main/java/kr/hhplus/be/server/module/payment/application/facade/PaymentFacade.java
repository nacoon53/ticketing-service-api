package kr.hhplus.be.server.module.payment.application.facade;

import kr.hhplus.be.server.module.auth.domain.service.WaitListTokenService;
import kr.hhplus.be.server.module.common.error.code.ErrorCode;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import kr.hhplus.be.server.module.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.module.concert.domain.service.ConcertService;
import kr.hhplus.be.server.module.payment.application.usecase.PaymentUsecase;
import kr.hhplus.be.server.module.payment.domain.service.PaymentService;
import kr.hhplus.be.server.module.user.domain.service.UserDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentFacade implements PaymentUsecase {

    private final ConcertService concertService;
    private final UserDepositService userDepositService;
    private final PaymentService paymentService;
    private final WaitListTokenService waitListTokenService;

    @Override
    public void payForConcert(String userId, String token, long reservationId) throws Exception {
        //예약이 유효한 지 확인
        if(!concertService.isValidReservation(reservationId)) {
            throw new ApiException(ErrorCode.REQUIRE_RESERVATION);
        }

        //예약 테이블과 좌석 테이블의 status 변경
        ConcertSeat seat = concertService.changeStatusToPaid(reservationId);

        //보유 금액 확인 후 포인트 차감
        userDepositService.useDeposit(userId, seat.getPrice());

        //결제 내역 저장
        paymentService.recordPaidPayment(userId, reservationId, seat.getPrice());

        //대기열 토큰 상태값 만료로 업데이트
        waitListTokenService.changeStatusToExpired(token);

    }
}
