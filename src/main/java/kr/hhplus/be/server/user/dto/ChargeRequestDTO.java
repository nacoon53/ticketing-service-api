package kr.hhplus.be.server.user.dto;

import lombok.Builder;
import lombok.Setter;

@Builder
@Setter
public class ChargeRequestDTO {
    private double amount;
}
