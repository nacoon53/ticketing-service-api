package kr.hhplus.be.server.module.auth.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.module.auth.domain.code.TokenStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Entity
@Table
public class WaitListToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;

    private String userId;
    private String token;

    @Column(name="token_status")
    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    @CreatedDate
    private LocalDateTime firstIssuedAt;

    @LastModifiedDate
    private LocalDateTime lastIssuedAt;

    @Builder
    WaitListToken(String userId, String token, TokenStatus status) {
        this.userId = userId;
        this.token = token;
        this.status = status;
    }


    public boolean isTokenActive() {
        if(StringUtils.equals(this.status.name(), TokenStatus.ACTIVE.name())) {
            return true;
        }
        return false;
    }

    public boolean isTokenDeactivated() {
        return !isTokenActive();
    }

    public void setTokenStatusToActive() {
        this.status = TokenStatus.ACTIVE;
    }

    public void setTokenStatusToExpired() {
        this.status = TokenStatus.EXPIRED;
    }
}
