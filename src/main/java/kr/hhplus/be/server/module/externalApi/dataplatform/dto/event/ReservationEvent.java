package kr.hhplus.be.server.module.externalApi.dataplatform.dto.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReservationEvent extends ApplicationEvent {
    private final String userId;
    private final long seatId;

    public ReservationEvent(Object source, String userId, long seatId) {
        super(source); // 이벤트 발생의 출처 (보통 this)
        this.userId = userId;
        this.seatId = seatId;
    }
}