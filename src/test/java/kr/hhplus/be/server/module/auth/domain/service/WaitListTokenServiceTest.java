package kr.hhplus.be.server.module.auth.domain.service;

import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WaitListTokenServiceTest {

    @Mock
    private WaitListTokenRepository waitListTokenRepository;

    @InjectMocks
    private WaitListTokenService waitListTokenService;

    private WaitListToken waitListToken;

    @BeforeEach
    void init() {
        waitListToken = WaitListToken.builder()
                .userId("test")
                .token("test:712ab784-0cd2-4913-839a-5ca2ecb89943:1")
                .status(TokenStatus.WAIT)
                .build();
    }

    @Test
    void 대기열_토큰을_생성한다() {
        // given
        String userId = "test";

        // when
        String generatedToken = waitListTokenService.generateWaitListToken(userId);

        // then
        assertThat(generatedToken).isNotNull();
        assertThat(generatedToken).contains(userId); // 토큰에 userId가 포함되어야 함
        assertThat(generatedToken).matches(String.format("%s:[a-f0-9-]+", userId));
    }

    @Test
    void 대기열_토큰_존재여부를_확인한다() {
        // given
        String userId = "test";
        given(waitListTokenRepository.findTopByUserIdAndStatusNotOrderByLastIssuedAtAsc(anyString(), eq(TokenStatus.EXPIRED))).willReturn(waitListToken);

        // when
        boolean result = waitListTokenService.isExistWaitListTokenByUserId(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 토큰으로_대기열_토큰을_가져온다() throws Exception {
        // given
        String token = "test:1";
        given(waitListTokenRepository.findByTokenWithLock(token)).willReturn(waitListToken);

        // when
        WaitListToken result = waitListTokenService.getWaitListTokenByToken(token);

        // then
        assertThat(result).isEqualTo(waitListToken);
    }

    @Test
    void 없는_토큰으로_대기열_토큰_조회시_예외가_발생한다() {
        // given
        String token = "invalidToken";
        given(waitListTokenRepository.findByTokenWithLock(token)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> waitListTokenService.getWaitListTokenByToken(token))
                .isInstanceOf(ApiException.class)
                .hasMessage("토큰이 존재하지 않습니다. 새로 발급 받아 주세요.");
    }

    @Test
    void 대기열_순번을_가져온다() {
        // given
        given(waitListTokenRepository.findByStatusOrderByLastIssuedAtAsc(TokenStatus.WAIT)).willReturn(List.of(waitListToken));

        // when
        long waitNumber = waitListTokenService.getWaitNumber(waitListToken.getToken());

        // then
        assertThat(waitNumber).isEqualTo(1);
    }

}