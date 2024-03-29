package fun.onysakura.algorithm.spring.security.repository;

import fun.onysakura.algorithm.spring.security.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findFirstByUsername(String username);
}
