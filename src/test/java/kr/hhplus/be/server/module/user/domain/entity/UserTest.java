package kr.hhplus.be.server.module.user.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Test
    void 유저가_보유한_예치금이_최대_보유_금액을_넘어가면_에러를_리턴한다() {
        //given
        User givenUser = User.builder()
                .deposit(0)
                .build();

        //when,then
        assertThatThrownBy(()->givenUser.increaseDeposit(100_000_000))
                .isInstanceOf(Exception.class)
                        .hasMessage("보유 금액이 최대 금액을 초과하였습니다");
    }

    @Test
    void 유저가_보유한_예치금에_3000원을_증가한다() throws Exception {
        //given
        User givenUser = User.builder()
                .deposit(0)
                .build();

        //when
        givenUser.increaseDeposit(3000);

        //then
        assertThat(givenUser.getDeposit()).isEqualTo(3000);
    }

    @Test
    void 유저가_보유한_예치금이_음수가_되면_에러를_리턴한다() {
        //given
        User givenUser = User.builder()
                .deposit(0)
                .build();

        //when,then
        assertThatThrownBy(()->givenUser.decreaseDeposit(1))
                .isInstanceOf(Exception.class)
                .hasMessage("보유 금액이 유효하지 않습니다.");
    }

    @Test
    void 유저가_보유한_예치금에_3000원을_감소한다() throws Exception {
        //given
        User givenUser = User.builder()
                .deposit(3000)
                .build();

        //when
        givenUser.decreaseDeposit(3000);

        //then
        assertThat(givenUser.getDeposit()).isEqualTo(0);
    }



}