package kr.hhplus.be.server.module.auth.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.policy.WaitListTokenPolicy;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.module.common.error.code.ErrorCode;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@Slf4j
public class WaitListTokenService {
    private WaitListTokenRepository waitListTokenRepository;
    private WaitListTokenPolicy waitListTokenPolicy;

    public WaitListTokenService(WaitListTokenRepository waitListTokenRepository, WaitListTokenPolicy waitListTokenPolicy) {
        this.waitListTokenRepository = waitListTokenRepository;
        this.waitListTokenPolicy = waitListTokenPolicy;
    }

    public void updateTokenTime(String token) {
        WaitListToken witListToken = waitListTokenRepository.findByToken(token);
        waitListTokenRepository.save(witListToken);
    }

    public WaitListToken generateWaitListToken(String userId) {
        List<WaitListToken> waitListTokens = waitListTokenRepository.findByStatusOrderByLastIssuedAtAsc(TokenStatus.WAIT);

        int waitNumber = 1;
        if(!CollectionUtils.isEmpty(waitListTokens)) {
            waitNumber = waitListTokens.size()+1;
        }

        String token = String.format("%s:%s:%s", userId, UUID.randomUUID(), waitNumber); //대기열토큰 값은 유저의 UUID와 대기 순번의 조합

        WaitListToken waitListToken = WaitListToken.builder()
                .userId(userId)
                .token(token)
                .status(TokenStatus.WAIT)
                .build();

        return waitListTokenRepository.save(waitListToken);
    }

    public boolean isExistWaitListTokenByUserId(String userId) {
        WaitListToken token = waitListTokenRepository.findTopByUserIdAndStatusNotOrderByLastIssuedAtAsc(userId, TokenStatus.EXPIRED);

        if(token == null) {
            return false;
        }

        return true;
    }

    @Transactional
    public WaitListToken getWaitListTokenByToken(String token) throws Exception {
        WaitListToken waitListToken = waitListTokenRepository.findByTokenWithLock(token);

        if(waitListToken == null || StringUtils.equals(waitListToken.getStatus().toString(), TokenStatus.EXPIRED.name())) {
            throw new ApiException(ErrorCode.NOT_FOUND_WAITLIST_TOKEN);
        }

        return waitListToken;
    }

    public int getWaitNumber(WaitListToken targetToken) {
        //상태값이 대기인 토큰들을 최종갱신시간으로 오름차순 정렬해서 나온 순서
        List<WaitListToken> waitListTokens = waitListTokenRepository.findByStatusOrderByLastIssuedAtAsc(TokenStatus.WAIT);

        int index = IntStream.range(0, waitListTokens.size())
                .filter(i -> waitListTokens.get(i).getToken().equals(targetToken.getToken()))
                .findFirst()
                .orElse(-2);

        return index+1;
    }

    public WaitListToken changeStatusToExpired(String token) {
        WaitListToken waitListToken = waitListTokenRepository.findByToken(token);

        waitListToken.setTokenStatusToExpired();

        return waitListTokenRepository.save(waitListToken);
    }

    public void expireTokens() {
        List<WaitListToken> activeTokens = waitListTokenRepository.findByStatusOrderByLastIssuedAtAsc(TokenStatus.ACTIVE);

        for(WaitListToken activeToken : activeTokens) {
            LocalDateTime targetTime = activeToken.getLastIssuedAt().plusMinutes(waitListTokenPolicy.getSessionMinutes());

            if(targetTime.isBefore(LocalDateTime.now())) {
                activeToken.setTokenStatusToExpired();
                waitListTokenRepository.save(activeToken);
            }
        }
    }

    @Transactional
    public void activeTokens() {
        Pageable pageable = PageRequest.of(0, waitListTokenPolicy.getMaxCapacity());
        List<WaitListToken> notExpiredTokens = waitListTokenRepository.findByStatusNotOrderByLastIssuedAtAsc(TokenStatus.EXPIRED, pageable);

        for(WaitListToken token : notExpiredTokens) {
            if(token.isTokenDeactivated()) {
                token.setTokenStatusToActive();
                waitListTokenRepository.save(token);
            }
        }
    }
}
