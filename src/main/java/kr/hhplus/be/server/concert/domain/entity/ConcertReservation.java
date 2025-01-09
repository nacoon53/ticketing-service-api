package kr.hhplus.be.server.concert.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.concert.domain.code.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table
public class ConcertReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reservationId;

    private long concertId;

    @Getter
    private long seatId;

    @Getter
    private String userId;

    @Getter
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime expiredAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    ConcertReservation(long seatId, String userId, ReservationStatus status, LocalDateTime expiredAt) {
        this.seatId = seatId;
        this.userId = userId;
        this.status = status;
        this.expiredAt = expiredAt;
    }

    public boolean isValidReservation(LocalDateTime time) {
        //이미 결제된 좌석인지 확인한다.
        if(this.status == ReservationStatus.PAID) {
            return false;
        }

        // 에약 만료 시간이 현재 시간보다 지났는지 확인한다.
        if(this.status == ReservationStatus.RESERVED && this.isExpiredTime(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    public boolean isExpiredTime(LocalDateTime time) {
        return expiredAt.isBefore(time);
    }

    public void changeStatusToPaid() {
        this.status = ReservationStatus.PAID;
    }
}
