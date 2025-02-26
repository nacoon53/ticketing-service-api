package kr.hhplus.be.server.dataplatformModule;

import kr.hhplus.be.server.module.externalApi.dataplatform.dto.event.ReservationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataPlatform {
    public void receivedReservationData(ReservationEvent event) {
        log.info("Received reservation data - userId: {}, seatId: {}", event.getUserId(), event.getSeatId());
        throw new RuntimeException();
    }

    @KafkaListener(topics = "finish-reservation", groupId = "test-group")
    public void listen(Object record) {
        System.out.println(record);
        //System.out.println("Received message: " + record.value());
    }
}