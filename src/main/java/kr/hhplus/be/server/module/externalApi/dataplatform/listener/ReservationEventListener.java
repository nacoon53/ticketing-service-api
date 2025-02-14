package kr.hhplus.be.server.module.externalApi.dataplatform.listener;

import kr.hhplus.be.server.dataplatformModule.DataPlatform;
import kr.hhplus.be.server.module.externalApi.dataplatform.dto.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReservationEventListener {
    private final DataPlatform dataPlatform;
    @Async
    @EventListener
    public void handleReservationEvent(ReservationEvent event) {
        dataPlatform.receivedReservationData(event);
    }
}