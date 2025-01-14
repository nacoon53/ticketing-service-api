package kr.hhplus.be.server.module.auth.application.usecase;

import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.presentation.dto.WaitListTokenValidationResponseDTO;

public interface WaitListTokenUsecase {
    //토큰 발급
    WaitListToken issueToken(String userId) throws Exception;

    //토큰 검증 및 대기 순번 가져오기
    WaitListTokenValidationResponseDTO checkTokenAndGetWaitNumber(String token) throws Exception;
}
