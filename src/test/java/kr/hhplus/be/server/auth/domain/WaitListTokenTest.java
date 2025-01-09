package kr.hhplus.be.server.auth.domain;

import kr.hhplus.be.server.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.auth.domain.entity.WaitListToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class WaitListTokenTest {
    @Test
    void 토큰상태가_ACTIVE이면_활성화상태이다() {
        //given
        WaitListToken token = WaitListToken.builder()
                .status(TokenStatus.ACTIVE)
                .build();

        //then
        assertTrue(token.isTokenActive());
        assertFalse(token.isTokenDeactivated());
    }

    @Test
    void 토큰상태가_ACTIVE가_아니면_비활성화상태이다() {
        //given
        WaitListToken token = WaitListToken.builder()
                .status(TokenStatus.WAIT)
                .build();

        //then
        assertTrue(token.isTokenDeactivated());
        assertFalse(token.isTokenActive());
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
        assertThat(token.getStatus()).isEqualTo(TokenStatus.EXPIRED);  // 상태가 EXPIRED로 변경되었는지 확인
    }
}