package kr.hhplus.be.server.user.service.facade;

import kr.hhplus.be.server.user.domain.entity.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class UserDepositFacadeTest {
    @Autowired
    private UserDepositFacade userDepositFacade;
    @Autowired
    private UserRepository userRepository;

//    public UserDepositFacadeTest(UserDepositFacade userDepositFacade, UserRepository userRepository) {
//        this.userDepositFacade = userDepositFacade;
//        this.userRepository = userRepository;
//    }

    String givenUserId = "test";
    double givenDeposit = 1000;

    @BeforeEach
    void init() {
        User user = User.builder()
                .id(givenUserId)
                .deposit(givenDeposit)
                .build();

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.clear();
    }

    @Test
    void 충전에_성공한다() throws Exception {
        //given
        double amount = 3000;

        //when
        userDepositFacade.chargeDeposit(givenUserId, amount);

        //then
        User user = userRepository.findById(givenUserId).get();
        assertThat(user.getDeposit()).isEqualTo(amount+givenDeposit);
    }

    @Test
    void 잔액을_가져온다() throws Exception {
        assertThat(userDepositFacade.getDeposit(givenUserId)).isEqualTo(givenDeposit);

        double amount = 3000;
        userDepositFacade.chargeDeposit(givenUserId, amount);
        assertThat(userDepositFacade.getDeposit(givenUserId)).isEqualTo(givenDeposit+amount);
    }

}