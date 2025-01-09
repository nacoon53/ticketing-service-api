package kr.hhplus.be.server.user.domain.repository;

import kr.hhplus.be.server.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String userId);
    User save(User user);
    void clear();
}
