package kr.hhplus.be.server.module.user.domain.service;


import kr.hhplus.be.server.module.common.error.code.ErrorCode;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import kr.hhplus.be.server.module.user.domain.entity.User;
import kr.hhplus.be.server.module.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserDepositService {
    private final UserRepository userRepository;

    @Transactional
    public void chargeDeposit(String userId, double amount) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

        user.increaseDeposit(amount);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public double getDeposit(String userId) {
        return userRepository.findById(userId)
                .map(User::getDeposit)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));
    }

    public void useDeposit(String userId, double amount) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

        user.decreaseDeposit(amount);
        userRepository.save(user);
    }
}
