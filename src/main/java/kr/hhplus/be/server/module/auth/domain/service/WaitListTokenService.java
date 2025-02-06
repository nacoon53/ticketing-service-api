package kr.hhplus.be.server.module.auth.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.config.web.RedisConfig;
import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.policy.WaitListTokenPolicy;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.module.auth.infrastructure.redis.WaitListTokenRedisManager;
import kr.hhplus.be.server.module.common.error.code.ErrorCode;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class WaitListTokenService {
    private final RedisConfig redisConfig;
    private final WaitListTokenRepository waitListTokenRepository;
    private final WaitListTokenPolicy waitListTokenPolicy;
    private final WaitListTokenRedisManager  waitListTokenRedisManager;


    public void updateTokenTime(String token) {
        WaitListToken witListToken = waitListTokenRepository.findByToken(token);
        waitListTokenRepository.save(witListToken);
    }

    public String generateWaitListToken(String userId) {
        return String.format("%s:%s", userId, UUID.randomUUID()); //대기열토큰 값은 유저의 UUID 조합
    }

    /***  토큰 저장하는 메서드
        1. redis에 저장
        2. redis에 저장 실패 시 db에 저장
     ***/
    public String saveWaitListToken(String userId, String token) {
        try {
            waitListTokenRedisManager.saveWaitToken(userId, token, waitListTokenPolicy.getSessionMinutes());
        } catch(Exception e) {
            WaitListToken waitListToken = WaitListToken.builder()
                    .userId(userId)
                    .token(token)
                    .status(TokenStatus.WAIT)
                    .build();
            waitListTokenRepository.save(waitListToken);
        }

        return token;
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

    /***
     *
     * @param token
     * @return
     *
     * 역할: 대기열 순번 조회
     *
     * - redis에서 조회
     * - redis가 에러날 경우 db에서 조회
     */
    public long getWaitNumber(String token) {

        try {
            Long rank = waitListTokenRedisManager.getWaitNumber(token);
            if(ObjectUtils.isEmpty(rank)) {
                return -2;
            }
           return rank + 1;

        } catch (Exception e) {
            //상태값이 대기인 토큰들을 최종갱신시간으로 오름차순 정렬해서 나온 순서
            List<WaitListToken> waitListTokens = waitListTokenRepository.findByStatusOrderByLastIssuedAtAsc(TokenStatus.WAIT);

            long index = IntStream.range(0, waitListTokens.size())
                    .filter(i -> waitListTokens.get(i).getToken().equals(token))
                    .findFirst()
                    .orElse(-2);

            return index+1;
        }

    }

    /***
     *
     * @param token
     *
     * 역할: 토큰의 상태값을 만료롭 변경
     * - redis에 저장된 토큰 삭제 및 expired로 변경
     * - db에 저장된 토큰 상태값 변경
     */
    public void changeStatusToExpired(String token) {
        // 레디스에 저장된 토큰 처리
        waitListTokenRedisManager.expireToken(token);

        // db에 저장된 토큰 처리
        WaitListToken waitListToken = waitListTokenRepository.findByToken(token);
        waitListToken.setTokenStatusToExpired();
        waitListTokenRepository.save(waitListToken);
    }

    /***
     * 역할: 시간 만료된 토큰의 상태값을 만료롭 변경
     * - redis에 저장된 토큰 삭제 및 expired로 변경
     * - db에 저장된 토큰 상태값 변경
     */
    public void expireTokens() {
        //redis에 저장된 토큰 삭제 및 expired로 변경
        Set<ZSetOperations.TypedTuple<Object>> activeTokensByRedis = waitListTokenRedisManager.getWaitListOfActiveTokens();

        for(ZSetOperations.TypedTuple<Object> tuple : activeTokensByRedis) {
            Double currentScore = tuple.getScore();
            Double updatedScore = currentScore + waitListTokenPolicy.getSessionMinutes()*60;

            if(updatedScore < System.currentTimeMillis()) {
                waitListTokenRedisManager.expireToken(tuple.getValue().toString());
            }
        }

        //db에 저장된 토큰 상태값 변경
        List<WaitListToken> activeTokens = waitListTokenRepository.findByStatusOrderByLastIssuedAtAsc(TokenStatus.ACTIVE);

        for(WaitListToken activeToken : activeTokens) {
            LocalDateTime expiredTime = activeToken.getLastIssuedAt().plusMinutes(waitListTokenPolicy.getSessionMinutes());

            if((LocalDateTime.now()).isAfter(expiredTime)) {
                activeToken.setTokenStatusToExpired();
                waitListTokenRepository.save(activeToken);
            }
        }
    }

    /***
     * 역할: 대기 상태의 토큰을 활성화로 변경
     * - redis에 저장된 토큰 상태값 변경
     * - db에 저장된 토큰 상태값 변경
     */
    @Transactional
    public void activeTokens() {
        // redis에 저장된 토큰 상태값을 활성화로 변경
        Set<ZSetOperations.TypedTuple<Object>> activeTokensByRedis = waitListTokenRedisManager.getWaitListOfActiveTokens();
        int avaliableActiveCount = waitListTokenPolicy.getMaxCapacity() - activeTokensByRedis.size();

        Set<Object> waitTokensByRedis = waitListTokenRedisManager.getWaitListOfWaitTokens(avaliableActiveCount);


        for(Object obj : waitTokensByRedis) {
            String token =  obj.toString().toString();
            waitListTokenRedisManager.activateToken(token);

            //db에 저장된 토큰 상태값 변경
            WaitListToken waitListToken = waitListTokenRepository.findByToken(token);
            waitListToken.setTokenStatusToActive();
            waitListTokenRepository.save(waitListToken);

        }
    }

    /***
     *
     * @param userId
     * @return
     *
     * 1. 해당 유저한테 유효한 토큰이 있는지 확인
     * 2. redis에 해당 유저의 토큰이 있는지 확인
     * 3. redis에서 에러가 나면 db에 해당 유저의 토큰이 있는지 확인
     */
    public boolean hasAnyWaitListToken(String userId) {

        try {
            return waitListTokenRedisManager.hasValidToken(userId);
        } catch(Exception e) { // TODO redis 관련에러가 맞는지 exception 구체화
            //토큰이 DB에 존재하는지 확인
            return this.isExistWaitListTokenByUserId(userId);
        }
    }

    /***
     *
     * @param userId
     * @param token
     *
     * 역할: 토큰의 상태값 반환
     *
     * 1. 기본적으로 redis로 조회하나
     * 2. redis에 에러가 나면 db 조회
     */
    public String getWaitListTokenStatus(String userId, String token) throws Exception {
        try {
            return waitListTokenRedisManager.getWaitListTokenStatusOfUser(userId);

        } catch(Exception e) {
            WaitListToken waitListToken = this.getWaitListTokenByToken(token);
            return waitListToken.getStatus().name();
        }

    }
}
