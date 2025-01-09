package kr.hhplus.be.server.concert.service;

import kr.hhplus.be.server.concert.domain.code.SeatStatus;
import kr.hhplus.be.server.concert.domain.entity.Concert;
import kr.hhplus.be.server.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.concert.domain.repository.ConcertRepository;
import kr.hhplus.be.server.concert.domain.repository.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final ConcertSeatRepository concertSeatRepository;

    public List<Concert> getConcertList() {
        return concertRepository.findByShowDateAfter(LocalDateTime.now());
    }

    public List<ConcertSeat> getAvailableSeat(long concertId) {
        return concertSeatRepository.findByConcertIdAndStatus(concertId, SeatStatus.AVAILABLE);

    }
}
