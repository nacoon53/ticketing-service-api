package kr.hhplus.be.server.module.auth.application.facade;

import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.module.auth.presentation.dto.WaitListTokenValidationResponseDTO;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import kr.hhplus.be.server.test.base.BaseIntegretionTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
class WaitListTokenFacadeTest extends BaseIntegretionTest {
    @Autowired
    private WaitListTokenFacade waitListTokenFacade;

    @Autowired
    private WaitListTokenRepository waitListTokenRepository;

    String userId = "test";
    String token = "test:1";

    @Test
    void 토큰_발급에_성공한다() throws Exception {
        //when
        String waitListToken = waitListTokenFacade.issueToken(userId);

        //then
        assertThat(waitListToken).isNotNull();
        assertThat(waitListToken).matches(String.format("%s:[a-f0-9-]+", userId, 1));
    }

    @Test
    void 대기열_순번을_성공적으로_가져온다() throws Exception {
        //given
        String waitListToken = waitListTokenFacade.issueToken(userId);

        //when
        WaitListTokenValidationResponseDTO dto = waitListTokenFacade.checkTokenAndGetWaitNumber(userId, waitListToken);

        //then
        assertThat(dto).isNotNull();
        assertTrue(dto.isValid());
        assertFalse(dto.isCanAccess());
        assertThat(dto.getWaitNumber()).isEqualTo(1);

    }

    @Test
    void  없는_토큰으로_대기열_순번을_가져와서_에러를_반환한다() throws Exception {
        //given
        String token = "nobody:1";

        //when, then
        assertThatThrownBy(()-> waitListTokenFacade.checkTokenAndGetWaitNumber(userId, token))
                .isInstanceOf(ApiException.class)
                .hasMessage("토큰이 존재하지 않습니다. 새로 발급 받아 주세요.");
    }
}