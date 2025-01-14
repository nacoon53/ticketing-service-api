package kr.hhplus.be.server.module.concert.infrastructure.repository.impl;

import kr.hhplus.be.server.module.concert.domain.entity.Concert;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertRepository;
import kr.hhplus.be.server.module.concert.infrastructure.repository.ConcertJpaRepository;
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
