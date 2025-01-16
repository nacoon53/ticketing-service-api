package kr.hhplus.be.server.module.auth.application.facade;

import kr.hhplus.be.server.module.auth.application.usecase.WaitListTokenUsecase;
import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.service.WaitListTokenService;
import kr.hhplus.be.server.module.auth.presentation.dto.WaitListTokenValidationResponseDTO;
import kr.hhplus.be.server.module.common.error.code.ErrorCode;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WaitListTokenFacade implements WaitListTokenUsecase {
    private final WaitListTokenService waitListTokenService;

    @Override
    public WaitListToken issueToken(String userId) throws Exception {
        //토큰이 DB에 존재하는지 확인
        if (waitListTokenService.isExistWaitListTokenByUserId(userId)) {
            throw new ApiException(ErrorCode.ALREADY_EXISTS_WAITLIST_TOKEN);
        }

        // 토큰 생성
        return waitListTokenService.generateWaitListToken(userId);
    }

    //대기열 순번 조회
    @Override
    public WaitListTokenValidationResponseDTO checkTokenAndGetWaitNumber(String token) throws Exception {
        //토큰이 DB에 존재하는지 확인
        WaitListToken waitListToken = waitListTokenService.getWaitListTokenByToken(token);

        //토큰이 활성화 상태면
        if (waitListToken.isTokenActive()) {
            return WaitListTokenValidationResponseDTO.builder()
                    .isValid(true)
                    .canAccess(true)
                    .waitNumber(0)
                    .build();
        }

        int waitNumber =waitListTokenService.getWaitNumber(waitListToken);

        return WaitListTokenValidationResponseDTO.builder()
                .isValid(true)
                .canAccess(false)
                .waitNumber(waitNumber)
                .build();
    }
}
