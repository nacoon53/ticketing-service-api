package kr.hhplus.be.server.module.auth.domain.entity;

import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WaitListTokenTest {
    @Test
    void 토큰상태가_ACTIVE이면_활성화상태이다() {
        //given
        WaitListToken token = WaitListToken.builder()
                .status(TokenStatus.ACTIVE)
                .build();

        //then
        Assertions.assertTrue(token.isTokenActive());
        Assertions.assertFalse(token.isTokenDeactivated());
    }

    @Test
    void 토큰상태가_ACTIVE가_아니면_비활성화상태이다() {
        //given
        WaitListToken token = WaitListToken.builder()
                .status(TokenStatus.WAIT)
                .build();

        //then
        Assertions.assertTrue(token.isTokenDeactivated());
        Assertions.assertFalse(token.isTokenActive());
    }


    @Test
    void 토큰상태를_만료로_변경_성공한다() {
        //given
        WaitListToken token = WaitListToken.builder()
                .status(TokenStatus.ACTIVE)
                .build();
        // when
        token.setTokenStatusToExpired();  // 상태 변경

        // then
        org.assertj.core.api.Assertions.assertThat(token.getStatus()).isEqualTo(TokenStatus.EXPIRED);  // 상태가 EXPIRED로 변경되었는지 확인
    }
}