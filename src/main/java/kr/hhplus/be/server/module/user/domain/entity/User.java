package kr.hhplus.be.server.module.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    private final double MAX_DEPOSIT = 100_000_000;

    public double increaseDeposit(double amount) throws Exception {
        double result = this.deposit + amount;

        if(result >= MAX_DEPOSIT) {
            throw new Exception("보유 금액이 최대 금액을 초과하였습니다");
        }

        this.deposit = result;
        return this.deposit;
    }

    public double decreaseDeposit(double amount) throws Exception {
        double result = this.deposit - amount;

        if(result < 0) {
            throw new Exception("보유 금액이 유효하지 않습니다.");
        }

        this.deposit = result;
        return this.deposit;
    }



}
