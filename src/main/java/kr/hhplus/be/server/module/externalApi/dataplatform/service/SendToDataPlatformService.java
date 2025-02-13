package kr.hhplus.be.server.module.externalApi.dataplatform.service;

import kr.hhplus.be.server.module.externalApi.dataplatform.dto.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendToDataPlatformService {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishReservationEvent(String userId, long seatId) {
        ReservationEvent event = new ReservationEvent(this, userId, seatId);
        applicationEventPublisher.publishEvent(event);
    }
}
