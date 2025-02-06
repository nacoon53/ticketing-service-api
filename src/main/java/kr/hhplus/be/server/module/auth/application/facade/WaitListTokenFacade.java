package kr.hhplus.be.server.module.auth.application.facade;

import io.micrometer.common.util.StringUtils;
import kr.hhplus.be.server.module.auth.application.usecase.WaitListTokenUsecase;
import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.module.auth.domain.policy.WaitListTokenPolicy;
import kr.hhplus.be.server.module.auth.domain.service.WaitListTokenService;
import kr.hhplus.be.server.module.auth.presentation.dto.WaitListTokenValidationResponseDTO;
import kr.hhplus.be.server.module.common.error.code.ErrorCode;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WaitListTokenFacade implements WaitListTokenUsecase {
    private final WaitListTokenService waitListTokenService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final WaitListTokenPolicy waitListTokenPolicy;

    /***
     *
     * @param userId
     * @return
     * @throws Exception
     *
     * 기능
     * 1. 이미 토큰을 발급 받았는지 확인
     * 2. 이미 토큰을 발급 받았다면 ERROR / 토큰을 발급 받지 않았다면 토큰 생성 후 저장 및 반환
     */
    @Override
    public String issueToken(String userId) throws Exception {

        // 해당 유저가 이미 토큰을 발급받았는지 확인
        if(waitListTokenService.hasAnyWaitListToken(userId)) {
            throw new ApiException(ErrorCode.ALREADY_EXISTS_WAITLIST_TOKEN);
        }

        // 토큰 생성
        String token = waitListTokenService.generateWaitListToken(userId);
        System.out.println("token : " + token);

        //토큰 저장
        waitListTokenService.saveWaitListToken(userId, token);

        return token;
    }

    /***
     *
     * @param userId
     * @param token
     * @return
     * @throws Exception
     *
     * 기능
     *  1. 토큰 상태 조회
     *  2. 대기열 순번 조회
     *  3. 토큰 상태와 대기열 순번 반환
     */
    @Override
    public WaitListTokenValidationResponseDTO checkTokenAndGetWaitNumber(String userId, String token) throws Exception {

        //토큰 상태 조회
        String tokenStatus = waitListTokenService.getWaitListTokenStatus(userId, token);

        if(StringUtils.isEmpty(tokenStatus)) {
            throw new ApiException(ErrorCode.NOT_FOUND_WAITLIST_TOKEN);
        }

        //토큰 상태가 만료면
        if(TokenStatus.EXPIRED.name().equals(tokenStatus)) {
            return WaitListTokenValidationResponseDTO.builder()
                    .isValid(false)
                    .canAccess(false)
                    .waitNumber(-1)
                    .build();
        }

        //토큰 상태가 활성화면
        if(TokenStatus.ACTIVE.name().equals(tokenStatus)) {
            return WaitListTokenValidationResponseDTO.builder()
                    .isValid(true)
                    .canAccess(true)
                    .waitNumber(0)
                    .build();
        }

        //토큰 상태 값이 대기면 대기열 순번 조회
        Long waitNumber = waitListTokenService.getWaitNumber(token);

        return WaitListTokenValidationResponseDTO.builder()
                .isValid(true)
                .canAccess(false)
                .waitNumber(waitNumber)
                .build();
    }
}
