package kr.hhplus.be.server.module.common.util;

import kr.hhplus.be.server.module.concert.domain.entity.Concert;
import kr.hhplus.be.server.module.concert.domain.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class BulkInsertUtil {
    private final ConcertRepository concertRepository;

    @Transactional
    public void insertDummyConcerts(int totalRecords) {
        int batchSize = 1000; // 한 번에 저장할 개수

        for (int i = 0; i < totalRecords; i += batchSize) {
            int size = Math.min(batchSize, totalRecords - i); // 마지막 배치 처리
            List<Concert> concerts = Stream.generate(() -> Instancio.of(Concert.class)
                            .ignore(Select.field(Concert::getId)) // ID 자동 증가
                            .generate(Select.field(Concert::getShowDate), gen ->
                                    gen.temporal().localDateTime().range(LocalDateTime.now(), LocalDateTime.now().plusYears(1))) // 오늘 ~ 1년 후 랜덤
                            .create())
                    .limit(size)
                    .collect(Collectors.toList());

            concertRepository.saveAll(concerts); // 💾 배치 저장
            System.out.println((i + size) + " records inserted...");
        }
        System.out.println("success insert records");
    }

}
