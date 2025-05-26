package goormthon_group4.backend.domain.user.repository;

import goormthon_group4.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long > {
    Optional<User> findByEmail(String email);
    Optional<User> findBySocialId(String socialId);
}
