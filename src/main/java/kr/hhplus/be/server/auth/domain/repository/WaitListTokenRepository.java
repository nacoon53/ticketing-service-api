package kr.hhplus.be.server.auth.domain.repository;

import kr.hhplus.be.server.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.auth.domain.entity.WaitListToken;

import java.util.List;

public interface WaitListTokenRepository {
    WaitListToken save(WaitListToken token);
    long count();
    WaitListToken findByUserId(String userId);

    WaitListToken findByToken(String token);

    List<WaitListToken> findByStatusOrderByLastIssuedAtAsc(TokenStatus tokenStatus);
}
