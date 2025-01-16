package kr.hhplus.be.server.module.concert.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.hhplus.be.server.module.concert.application.usecase.ConcertUsecase;
import kr.hhplus.be.server.module.concert.presentation.dto.AvailableSeatResponseDTO;
import kr.hhplus.be.server.module.concert.presentation.dto.BookingRequestDTO;
import kr.hhplus.be.server.module.concert.presentation.dto.ConcertReservationResponseDTO;
import kr.hhplus.be.server.module.concert.presentation.dto.ConcertResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name="콘서트 API", description = "콘서트 관련 정보를 제공합니다.")
@RequestMapping("/api/v1/concerts")
public class ConcertController {
    private final ConcertUsecase concertUsecase;

    // 콘서트 목록(공연 날짜 포함) 조회
    @Operation(description = "콘서트 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ConcertResponseDTO>> getConcertList() {
        List<ConcertResponseDTO> list = concertUsecase.getConcertList();

        return ResponseEntity.ok(list);
    }

    // 예약 가능 좌석 조회
    @Operation(description = "선택한 콘서트에 예약 가능한 좌석을 조회합니다.")
    @GetMapping("/{concertId}/seats")
    public ResponseEntity<List<AvailableSeatResponseDTO>> getAvailableSeats(@PathVariable("concertId") long concertId,
                                                                            @RequestParam("status") String status) {
        if(!"available".equals(status)) {
            return ResponseEntity.badRequest().build();
        }
        List<AvailableSeatResponseDTO> list = concertUsecase.getAvailableSeat
                (concertId);

        return ResponseEntity.ok(list);
    }

    // 좌석 예약
    @Operation(description = "선택한 좌석에 대해 임시 배정 처리합니다.")
    @PostMapping("/reservation")
    public ResponseEntity<?> bookSeat(@RequestBody BookingRequestDTO dto, HttpServletRequest request) throws Exception {
        String userId = (String) request.getAttribute("loginId");
        String token = (String) request.getAttribute("token");

        ConcertReservationResponseDTO responseDTO = concertUsecase.reserveSeat(userId, dto.seatId(), token);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
