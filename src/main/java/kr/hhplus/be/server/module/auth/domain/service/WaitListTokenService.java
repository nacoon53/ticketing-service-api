package kr.hhplus.be.server.module.auth.domain.service;

import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class WaitListTokenService {
    private final WaitListTokenRepository waitListTokenRepository;

    public void updateTokenTime(String token) {
        WaitListToken witListToken = waitListTokenRepository.findByToken(token);
        waitListTokenRepository.save(witListToken);
    }

    public WaitListToken generateWaitListToken(String userId) {
       // String uuid = UUID.randomUUID().toString();
        List<WaitListToken> waitListTokens = waitListTokenRepository.findByStatusOrderByLastIssuedAtAsc(TokenStatus.WAIT);

        int waitNumber = 1;
        if(!CollectionUtils.isEmpty(waitListTokens)) {
            waitNumber = waitListTokens.size()+1;
        }

        String token = String.format("%s:%s", userId, waitNumber); //대기열토큰 값은 유저의 UUID와 대기 순번의 조합

        WaitListToken waitListToken = WaitListToken.builder()
                .userId(userId)
                .token(token)
                .status(TokenStatus.WAIT)
                .build();

        return waitListTokenRepository.save(waitListToken);
    }

    public boolean isExistWaitListTokenByUserId(String userId) {
        WaitListToken token = waitListTokenRepository.findByUserId(userId);

        if(token == null) {
            return false;
        }

        return true;
    }

    public WaitListToken getWaitListTokenByToken(String token) throws Exception {
        WaitListToken waitListToken = waitListTokenRepository.findByToken(token);

        if(waitListToken == null || StringUtils.equals(waitListToken.getStatus().toString(), TokenStatus.EXPIRED.name())) {
            throw new Exception("토큰이 존재하지 않습니다. 새로 발급 받아 주세요.");
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
}