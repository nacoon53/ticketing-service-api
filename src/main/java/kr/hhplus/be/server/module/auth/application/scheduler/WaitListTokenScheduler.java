package kr.hhplus.be.server.module.auth.application.scheduler;

import kr.hhplus.be.server.module.auth.domain.service.WaitListTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WaitListTokenScheduler {
    private final WaitListTokenService waitListTokenService;

    @Scheduled(fixedRate = 5000)
    public void schedule() {
        //유효기간이 만료된 토큰 expired 처리
        waitListTokenService.expireTokens();

        //토큰 active처리
        waitListTokenService.activeTokens();

    }
}

