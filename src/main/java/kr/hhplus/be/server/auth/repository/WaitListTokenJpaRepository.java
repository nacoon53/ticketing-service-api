package kr.hhplus.be.server.auth.repository;

import kr.hhplus.be.server.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.auth.domain.entity.WaitListToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaitListTokenJpaRepository extends JpaRepository<WaitListToken, Long> {
    WaitListToken findByUserId(String userId);
    WaitListToken findByToken(String token);

    List<WaitListToken> findByStatusOrderByLastIssuedAtAsc(TokenStatus tokenStatus);
}
