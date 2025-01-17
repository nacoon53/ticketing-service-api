package kr.hhplus.be.server.module.concert.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.module.concert.domain.code.SeatStatus;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class ConcertSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="seat_id")
    private long id;

    private long concertId;

    @Column(name="seat_number")
    private int number;

    
    @Column(name="seat_status")
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    private double price;

    @Setter
    private LocalDateTime holdExpiredAt;

    @Builder
    public ConcertSeat(long concertId, int number, SeatStatus status) {
        this.concertId = concertId;
        this.number = number;
        this.status = status;
    }

    public boolean isSeatAvailable() {
        //예약 가능한 상태인 경우
        if(StringUtils.equals(this.status.toString(), SeatStatus.AVAILABLE.name())) {
            return true;
        }

        //임시 배정된 좌석의 경우 좌석 선점 시간이 만료 되지 않았다면
//        if(StringUtils.equals(this.status.toString(), SeatStatus.TEMP_ASSIGNED.name())
//                && isSeatReservationValidTime ()) {
        return false;
    }

    public boolean isSeatReservationValidTime () {
        return holdExpiredAt != null && holdExpiredAt.isAfter(LocalDateTime.now());
    }

    public boolean isSeatOccupied() {
        return !isSeatAvailable();
    }

    public void changeStatusToTempAssigned(LocalDateTime expiredAt) {
        this.status = SeatStatus.TEMP_ASSIGNED;
        this.holdExpiredAt = expiredAt;
    }

    public void changeStatusToPaid() {
        this.status = SeatStatus.PAID;
        this.holdExpiredAt = null;
    }
}
