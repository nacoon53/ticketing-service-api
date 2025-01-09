package kr.hhplus.be.server.auth.service.facade;

import kr.hhplus.be.server.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.auth.domain.usecase.WaitListTokenUsecase;
import kr.hhplus.be.server.auth.dto.WaitListTokenValidationResponseDTO;
import kr.hhplus.be.server.auth.service.WaitListTokenService;
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
            throw new Exception("이미 발급받은 대기열 토큰이 존재합니다.");
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
int a =waitListTokenService.getWaitNumber(waitListToken);

        return WaitListTokenValidationResponseDTO.builder()
                .isValid(true)
                .canAccess(false)
                .waitNumber(waitListTokenService.getWaitNumber(waitListToken))
                .build();
    }
}
