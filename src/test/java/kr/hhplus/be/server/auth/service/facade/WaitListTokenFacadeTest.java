package kr.hhplus.be.server.auth.service.facade;

import kr.hhplus.be.server.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.auth.dto.WaitListTokenValidationResponseDTO;
import kr.hhplus.be.server.test.base.BaseIntegretionTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
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
        WaitListToken waitListToken = waitListTokenFacade.issueToken(userId);

        //then
        assertThat(waitListToken).isNotNull();
        assertThat(waitListToken.getToken()).isEqualTo(token);
    }

    @Test
    void 대기열_순번을_성공적으로_가져온다() throws Exception {
        //given
        WaitListToken waitListToken = waitListTokenFacade.issueToken(userId);
        waitListTokenRepository.save(waitListToken);

        //when
        WaitListTokenValidationResponseDTO dto = waitListTokenFacade.checkTokenAndGetWaitNumber(token);

        //then
        assertThat(dto).isNotNull();
        assertTrue(dto.isValid());
        //assertTrue(dto.isCanAccess()); //TODO 토큰 스케줄러 필요
        assertThat(dto.getWaitNumber()).isEqualTo(1);

    }
}