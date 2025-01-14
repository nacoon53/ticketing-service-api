package kr.hhplus.be.server.module.auth.infrastructure.repository.impl;

import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import kr.hhplus.be.server.module.auth.domain.repository.WaitListTokenRepository;
import kr.hhplus.be.server.module.auth.infrastructure.repository.WaitListTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JpaWaitListTokenRepositoryImpl implements WaitListTokenRepository {
    private final WaitListTokenJpaRepository waitListTokenJpaRepository;

    @Override
    public WaitListToken save(WaitListToken token) {
        return waitListTokenJpaRepository.save(token);
    }

    @Override
    public long count() {
       return waitListTokenJpaRepository.count();
    }

    @Override
    public WaitListToken findByUserId(String userId) {
        return waitListTokenJpaRepository.findByUserId(userId);
    }

    @Override
    public WaitListToken findByToken(String token) {
        return waitListTokenJpaRepository.findByToken(token);
    }

    @Override
    public List<WaitListToken> findByStatusOrderByLastIssuedAtAsc(TokenStatus tokenStatus) {
        return waitListTokenJpaRepository.findByStatusOrderByLastIssuedAtAsc(tokenStatus);
    }
}