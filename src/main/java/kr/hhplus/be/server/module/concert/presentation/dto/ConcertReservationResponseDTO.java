package kr.hhplus.be.server.module.concert.presentation.dto;

import kr.hhplus.be.server.module.concert.domain.entity.ConcertReservation;
import lombok.Builder;

@Builder
public record ConcertReservationResponseDTO (
        long reservationId
){
    public static ConcertReservationResponseDTO fromEntity(ConcertReservation reservation) {
        return ConcertReservationResponseDTO.builder()
                .reservationId(reservation.getReservationId())
                .build();
    }

}
