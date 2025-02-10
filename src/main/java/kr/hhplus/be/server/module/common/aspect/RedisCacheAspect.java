package kr.hhplus.be.server.module.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.Arrays;

@Aspect
@Component
public class RedisCacheAspect {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(kr.hhplus.be.server.module.common.aspect.annotation.CacheableRedis)")
    public Object cacheHandler(ProceedingJoinPoint joinPoint) throws Throwable{
        String key = generenateKey(joinPoint);
        Object cachedValue = redisTemplate.opsForValue().get(key);

        if(!ObjectUtils.isEmpty(cachedValue)) {
            System.out.println("Cache Hit");
            return cachedValue;
        }

        //캐시에 값이 없으면
        Object result = joinPoint.proceed();
        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(10)); //캐싱 시간 지정

        return result;
    }

    private String generenateKey(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        return methodName + ":" + args;
    }
}
