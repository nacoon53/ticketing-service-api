package kr.hhplus.be.server.concert.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.concert.dto.AvailableSeatResponseDTO;
import kr.hhplus.be.server.concert.dto.BookingRequestDTO;
import kr.hhplus.be.server.concert.dto.ConcertResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="콘서트 API", description = "콘서트 관련 정보를 제공합니다.")
@RequestMapping("/api/v1/concerts")
public class ConcertController {

    // 콘서트 목록(공연 날짜 포함) 조회
    @Operation(description = "콘서트 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ConcertResponseDTO>> getConcertList() {
        ConcertResponseDTO concert1 = ConcertResponseDTO.builder()
                .concertId(0L)
                .concertName("씨엔블루 콘서트")
                .host("씨엔블루")
                .showDate("202510071300")
                .build();

        ConcertResponseDTO concert2 = ConcertResponseDTO.builder()
                .concertId(1L)
                .concertName("세븐틴 콘서트")
                .host("세븐틴")
                .showDate("202501111300")
                .build();
        return ResponseEntity.ok(List.of(concert1, concert2));
    }

    // 예약 가능 좌석 조회
    @Operation(description = "선택한 콘서트에 예약 가능한 좌석을 조회합니다.")
    @GetMapping("/{concertId}/seats")
    public ResponseEntity<List<AvailableSeatResponseDTO>> getAvailableSeats(@PathVariable("concertId") long concertId,
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
    @Operation(description = "선택한 좌석에 대해 임시 배정 처리합니다.")
    @PostMapping("/reservation")
    public ResponseEntity<String> bookSeat(@RequestBody BookingRequestDTO request) {
        int status = 200;
        String msg = "요청이 정상적으로 처리되었습니다.";
        return ResponseEntity.status(status == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(msg);
    }
}
