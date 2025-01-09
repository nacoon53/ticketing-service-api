package kr.hhplus.be.server.user.service;


import kr.hhplus.be.server.user.domain.entity.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserDepositService {
    private final UserRepository userRepository;

    @Transactional
    public void chargeDeposit(String userId, double amount) throws Exception {
        User user = userRepository.findById(userId).get();

        user.increaseDeposit(amount);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public double getDeposit(String userId) {
        User user = userRepository.findById(userId).get();
        return user.getDeposit();
    }

    public void useDeposit(String userId, double amount) throws Exception {
        User user = userRepository.findById(userId).get();

        user.decreaseDeposit(amount);
        userRepository.save(user);
    }
}
