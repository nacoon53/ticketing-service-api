package kr.hhplus.be.server.module.user.presentation.dto;

import lombok.Builder;
import lombok.Setter;

@Builder
@Setter
public class ChargeRequestDTO {
    private double amount;
}
