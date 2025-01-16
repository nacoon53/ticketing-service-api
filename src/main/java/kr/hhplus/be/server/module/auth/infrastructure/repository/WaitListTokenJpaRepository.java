package kr.hhplus.be.server.module.auth.infrastructure.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import kr.hhplus.be.server.module.auth.domain.entity.WaitListToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaitListTokenJpaRepository extends JpaRepository<WaitListToken, Long> {
    WaitListToken findByUserId(String userId);
    WaitListToken findByToken(String token);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WaitListToken w WHERE w.token = :token")
    WaitListToken findByTokenWithLock(@Param("token") String token);

    List<WaitListToken> findByStatusOrderByLastIssuedAtAsc(TokenStatus tokenStatus);
    List<WaitListToken> findByStatusNotOrderByLastIssuedAtAsc(TokenStatus tokenStatus, Pageable pageable);
    WaitListToken findTopByUserIdAndStatusNotOrderByLastIssuedAtAsc(String userId, TokenStatus status);
}
