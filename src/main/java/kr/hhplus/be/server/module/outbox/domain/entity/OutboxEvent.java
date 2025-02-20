package kr.hhplus.be.server.module.outbox.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "outbox_event")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;  // 엔티티 타입 (예: "Order")
    private Long aggregateId;      // 엔티티 ID (예: 주문 ID)

    private String eventType;      // 이벤트 타입 (예: "OrderCreated")

    private String status; //이벤트 처리 상태
    @Lob
    @Column(columnDefinition = "TEXT")
    private String payload;        // JSON 데이터 저장

    private LocalDateTime createdAt = LocalDateTime.now();
}
