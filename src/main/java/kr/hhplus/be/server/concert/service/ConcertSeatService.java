package kr.hhplus.be.server.concert.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.concert.domain.repository.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ConcertSeatService {
    private final ConcertSeatRepository concertSeatRepository;

    @Transactional
    public ConcertSeat chageStatusToTempAssigned(long seatId, LocalDateTime expiredAt) throws Exception{
        ConcertSeat seat = concertSeatRepository.findById(seatId).get();

        //좌석 유효성 검사
        if(seat.isSeatOccupied()) { //다른 사람이 예약했거나 이미 결제한 경우
            throw new Exception("이미 선점된 좌석입니다.");
        }

        // 좌석을 임시 배정으로 변경 && 예약 만료 시간 업데이트
        seat.changeStatusToTempAssigned(expiredAt);

        return concertSeatRepository.save(seat);
    }

    public ConcertSeat changeStatusToPaid(long seatId) {
        ConcertSeat seat = concertSeatRepository.findById(seatId).get();

        // 좌석을 선점 상태로 변경
        seat.changeStatusToPaid();

        return concertSeatRepository.save(seat);
    }
}
