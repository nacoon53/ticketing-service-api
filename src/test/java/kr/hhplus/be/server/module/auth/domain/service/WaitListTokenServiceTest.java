package kr.hhplus.be.server.module.auth.domain.service;

import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
                .token("test:1")
                .status(TokenStatus.WAIT)
                .build();
    }

    @Test
    void 대기열_토큰을_생성한다() {
        // given
        String userId = "test";
        BDDMockito.given(waitListTokenRepository.findByStatusOrderByLastIssuedAtAsc(TokenStatus.WAIT)).willReturn(List.of(waitListToken));
        BDDMockito.given(waitListTokenRepository.save(ArgumentMatchers.any(WaitListToken.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        WaitListToken generatedToken = waitListTokenService.generateWaitListToken(userId);

        // then
        Assertions.assertThat(generatedToken).isNotNull();
        Assertions.assertThat(generatedToken.getToken()).contains(userId); // 토큰에 userId가 포함되어야 함
        Assertions.assertThat(generatedToken.getToken()).isEqualTo("test:2");
    }

    @Test
    void 대기열_토큰_존재여부를_확인한다() {
        // given
        String userId = "test";
        BDDMockito.given(waitListTokenRepository.findByUserId(userId)).willReturn(waitListToken);

        // when
        boolean result = waitListTokenService.isExistWaitListTokenByUserId(userId);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void 토큰으로_대기열_토큰을_가져온다() throws Exception {
        // given
        String token = "test:1";
        BDDMockito.given(waitListTokenRepository.findByToken(token)).willReturn(waitListToken);

        // when
        WaitListToken result = waitListTokenService.getWaitListTokenByToken(token);

        // then
        Assertions.assertThat(result).isEqualTo(waitListToken);
    }

    @Test
    void 없는_토큰으로_대기열_토큰_조회시_예외가_발생한다() {
        // given
        String token = "invalidToken";
        BDDMockito.given(waitListTokenRepository.findByToken(token)).willReturn(null);

        // when & then
        Assertions.assertThatThrownBy(() -> waitListTokenService.getWaitListTokenByToken(token))
                .isInstanceOf(Exception.class)
                .hasMessage("토큰이 존재하지 않습니다. 새로 발급 받아 주세요.");
    }

    @Test
    void 대기열_순번을_가져온다() {
        // given
        BDDMockito.given(waitListTokenRepository.findByStatusOrderByLastIssuedAtAsc(TokenStatus.WAIT)).willReturn(List.of(waitListToken));

        // when
        int waitNumber = waitListTokenService.getWaitNumber(waitListToken);

        // then
        Assertions.assertThat(waitNumber).isEqualTo(1);
    }

}