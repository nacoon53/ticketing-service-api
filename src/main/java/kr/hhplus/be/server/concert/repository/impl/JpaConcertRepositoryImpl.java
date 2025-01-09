package kr.hhplus.be.server.concert.repository.impl;

import kr.hhplus.be.server.concert.domain.entity.Concert;
import kr.hhplus.be.server.concert.domain.repository.ConcertRepository;
import kr.hhplus.be.server.concert.repository.ConcertJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class JpaConcertRepositoryImpl implements ConcertRepository {
    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public List<Concert> findByShowDateAfter(LocalDateTime targetDateTime) {
        return concertJpaRepository.findByShowDateAfter(targetDateTime);
    }

    @Override
    public Concert save(Concert concert) {
        return concertJpaRepository.save(concert);
    }
}
