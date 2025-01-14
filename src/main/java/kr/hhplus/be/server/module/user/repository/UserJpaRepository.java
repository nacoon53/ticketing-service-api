package kr.hhplus.be.server.module.user.repository;

import kr.hhplus.be.server.module.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, String> {
}
