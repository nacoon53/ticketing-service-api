package kr.hhplus.be.server.concert.controller;

import kr.hhplus.be.server.concert.dto.AvailableSeatResponseDTO;
import kr.hhplus.be.server.concert.dto.BookingRequestDTO;
import kr.hhplus.be.server.concert.dto.ConcertResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/concert")
public class ConcertController {

    // 콘서트 목록 조회
    @GetMapping
    public ResponseEntity<List<ConcertResponseDTO>> getConcertList() {
        ConcertResponseDTO concert1 = ConcertResponseDTO.builder()
                .concertId(0L)
                .concertName("씨엔블루 콘서트")
                .host("씨엔블루")
                .build();

        ConcertResponseDTO concert2 = ConcertResponseDTO.builder()
                .concertId(1L)
                .concertName("세븐틴 콘서트")
                .host("세븐틴")
                .build();
        return ResponseEntity.ok(List.of(concert1, concert2));
    }

    // 예약 가능 날짜 조회
    @GetMapping("/{concertId}/date")
    public ResponseEntity<List<String>> getAvailableDates(@PathVariable("concertId") long concertId,
                                                          @RequestParam("status") String status) {
        if(!"available".equals(status)) {
            return ResponseEntity.badRequest().build();
        }

        List<String> availableDates = List.of("20241007", "20241008");
        return ResponseEntity.ok(availableDates);
    }

    // 예약 가능 좌석 조회
    @GetMapping("/{concertId}/{concertDate}/seat")
    public ResponseEntity<List<AvailableSeatResponseDTO>> getAvailableSeats(@PathVariable("concertId") long concertId,
                                                                            @PathVariable("concertDate") String concertDate,
                                                                            @RequestParam("status") String status) {
        if(!"available".equals(status)) {
            return ResponseEntity.badRequest().build();
        }

        AvailableSeatResponseDTO seat1 = AvailableSeatResponseDTO.builder()
                .seatId(0L)
                .seatNumber(1)
                .price(1000)
                .build();

        AvailableSeatResponseDTO seat2 = AvailableSeatResponseDTO.builder()
                .seatId(1L)
                .seatNumber(2)
                .price(2000)
                .build();

        return ResponseEntity.ok(List.of(seat1, seat2));
    }

    // 좌석 예약
    @PostMapping("/booking")
    public ResponseEntity<String> bookSeat(@RequestBody BookingRequestDTO request) {
        int status = 200;
        String msg = "요청이 정상적으로 처리되었습니다.";
        return ResponseEntity.status(status == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(msg);
    }
}
