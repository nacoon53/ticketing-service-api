package kr.hhplus.be.server.concert.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConcertResponseDTO {
    private long concertId;
    private String concertName;
    private String host;
    private String showDate;
}
