package kr.hhplus.be.server.module.auth.application.facade;

import kr.hhplus.be.server.module.auth.application.scheduler.WaitListTokenScheduler;
import kr.hhplus.be.server.module.auth.application.usecase.WaitListTokenUsecase;
import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.policy.WaitListTokenPolicy;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.module.auth.domain.service.WaitListTokenService;
import kr.hhplus.be.server.module.auth.infrastructure.redis.WaitListTokenRedisManager;
import kr.hhplus.be.server.module.user.domain.entity.User;
import kr.hhplus.be.server.module.user.domain.repository.UserRepository;
import kr.hhplus.be.server.test.base.BaseIntegretionTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class WaitListTokenFacadeConcurrencyTest extends BaseIntegretionTest {
    @Autowired
    private WaitListTokenUsecase waitListTokenUsecase;
    @Autowired
    private WaitListTokenRepository waitListTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WaitListTokenRedisManager waitListTokenRedisManager;
    @Mock
    private WaitListTokenPolicy waitListTokenPolicy;
    @Autowired
    private WaitListTokenService waitListTokenService;
    @Autowired
    private WaitListTokenScheduler waitListTokenScheduler;

    User givenUser;

    String userId = "test";
    String token = "test:1";

    @BeforeEach
    void init() {
        User user = User.builder()
                .id(userId)
                .deposit(50_000)
                .build();

        givenUser = userRepository.save(user);

        WaitListToken waitListToken = WaitListToken.builder()
                .userId(userId)
                .token(token)
                .build();

        waitListTokenRepository.save(waitListToken);

    }

    @Test
    void 사용자_20명이_요청을_하면_10명만_액세스한다() throws Exception {
        //given
        int threadCount = 20;
        int capacity = 10;
        when(waitListTokenPolicy.getMaxCapacity()).thenReturn(capacity);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Map<String, String> tokenList = new HashMap<>();

        //when
        for (int i = 0; i < threadCount; i++) {
            String userId = "user" + i;
            executorService.execute(() -> {
                try {
                    String token = waitListTokenUsecase.issueToken(userId);
                    tokenList.put(userId, token);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();  // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        Thread.sleep(10000);

        //then
        int activeCount = 0;
        for(int i=0; i<threadCount; i++) {
            String userId = "user" + i;
            String status = waitListTokenService.getWaitListTokenStatus(userId, token);

            if(StringUtils.equals(TokenStatus.ACTIVE.name(), status)) {
                activeCount++;
            }
        }

        //토큰 값 확인
        assertThat(activeCount).isEqualTo(capacity);
    }

}