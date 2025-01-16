package kr.hhplus.be.server.module.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.hhplus.be.server.module.common.error.code.ErrorCode;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class User {
    @Id
    @Column(name="user_id")
    private String id;

    @Getter
    private double deposit;

    private static final double MAX_DEPOSIT = 100_000_000;

    public double increaseDeposit(double amount) throws Exception {
        double result = this.deposit + amount;

        if(result >= MAX_DEPOSIT) {
            throw new ApiException(ErrorCode.OVER_MAXIMUM_DEPOSIT);
        }

        this.deposit = result;
        return this.deposit;
    }

    public double decreaseDeposit(double amount) throws Exception {
        double result = this.deposit - amount;

        if(result < 0) {
            throw new ApiException(ErrorCode.BAD_REQUEST_FOR_DEPOSIT);
        }

        this.deposit = result;
        return this.deposit;
    }



}
