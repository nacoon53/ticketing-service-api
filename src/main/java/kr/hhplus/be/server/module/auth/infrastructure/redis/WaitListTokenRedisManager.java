package kr.hhplus.be.server.module.auth.infrastructure.redis;

import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class WaitListTokenRedisManager {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String KEY_OF_WAITLIST_USER_TOKEN = "waitlist:status:";
    private final String KEY_OF_WAITLIST_FOR_WAIT = "waitlist:wait"; // -> 대기열의 순번이 필요하기 때문에 sortedset 이용
    private final String KEY_OF_WAITLIST_FOR_ACTIVE = "waitlist:active"; //-> 만료될 때 스케줄러에서 score 값을 보고 만료시키기 때문에 sortedset이용

    private String getUserKey(String userId) {
        return KEY_OF_WAITLIST_USER_TOKEN + userId;
    }

    public boolean hasValidToken(String userId) {
        String key = getUserKey(userId);
        String value = (String) redisTemplate.opsForValue().get(key);
        return !ObjectUtils.isEmpty(value) && !StringUtils.equals(value, TokenStatus.EXPIRED.name());
    }

    public void saveWaitToken(String userId, String token, long sessionMinutes) {
        String key = getUserKey(userId);
        redisTemplate.opsForValue().set(key, TokenStatus.WAIT.name());
        redisTemplate.opsForZSet().add(KEY_OF_WAITLIST_FOR_WAIT, token, System.currentTimeMillis());
        //대기 토큰에 저장된 후 일정 시간 내에 아무것도 하지 않으면 삭제 처리
        redisTemplate.expire(key, sessionMinutes, TimeUnit.MINUTES);
    }

    public Long getWaitNumber(String token) {
        return redisTemplate.opsForZSet().rank(KEY_OF_WAITLIST_FOR_WAIT, token) ;
    }

    public void expireToken(String token) {
        String userId = getUserIdFromToken(token);
        String key = getUserKey(userId);

        // 캐시 스탬피드 현상 방지
        redisTemplate.opsForValue().set(key, TokenStatus.EXPIRED.name());
        redisTemplate.expire(key, 30, TimeUnit.SECONDS);

        redisTemplate.opsForZSet().remove(KEY_OF_WAITLIST_FOR_ACTIVE, token);
    }

    public void activateToken(String token) {
        String userId = getUserIdFromToken(token);
        String key = getUserKey(userId);
        redisTemplate.opsForValue().set(key, TokenStatus.ACTIVE.name());

        redisTemplate.opsForZSet().remove(KEY_OF_WAITLIST_FOR_WAIT, token);
        redisTemplate.opsForZSet().add(KEY_OF_WAITLIST_FOR_ACTIVE, token, System.currentTimeMillis());

    }

    public String getWaitListTokenStatusOfUser(String userId) {
        String key = getUserKey(userId);
        String value = (String) redisTemplate.opsForValue().get(key);
        return value;
    }

    public Set<Object> getWaitListOfWaitTokens(int avaliableActiveCount) {
        return redisTemplate.opsForZSet().range(KEY_OF_WAITLIST_FOR_WAIT, 0, avaliableActiveCount-1);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getWaitListOfActiveTokens() {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(KEY_OF_WAITLIST_FOR_WAIT, 0, -1);
    }


    public int getActiveTokenSize() {
        return redisTemplate.opsForZSet().size(KEY_OF_WAITLIST_FOR_ACTIVE).intValue();
    }

    private String getUserIdFromToken(String token) {
        return  token.split(":")[0];
    }

}
