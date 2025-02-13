package kr.hhplus.be.server.module.concert.domain.repository;

import kr.hhplus.be.server.module.concert.domain.entity.Concert;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertRepository {
    List<Concert> findByShowDateAfter(LocalDateTime targetDateTime);
    Concert save(Concert concert);
    List<Concert> saveAll(List<Concert> list);
}
