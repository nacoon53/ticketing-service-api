package kr.hhplus.be.server.module.concert.presentation.dto;

import kr.hhplus.be.server.module.common.util.DateTimeFormatterUtil;
import kr.hhplus.be.server.module.concert.domain.entity.Concert;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConcertResponseDTO {
    private long concertId;
    private String concertName;
    private String host;
    private String showDate;

    public static ConcertResponseDTO fromEntity(Concert concert) {
        return ConcertResponseDTO.builder()
                .concertId(concert.getId())
                .concertName(concert.getConcertName())
                .host(concert.getHost())
                .showDate(DateTimeFormatterUtil.formatLocalDateTime(concert.getShowDate()))
                .build();
    }
}
