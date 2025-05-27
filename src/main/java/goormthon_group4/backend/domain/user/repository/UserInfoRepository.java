package goormthon_group4.backend.domain.user.repository;

import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    Optional<UserInfo> findByUser(User user);
    Optional<UserInfo> findByUserId(Long userId);
    boolean existsByNickname(String nickname);
}
