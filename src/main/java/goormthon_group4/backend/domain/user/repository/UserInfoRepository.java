package goormthon_group4.backend.domain.user.repository;

import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    boolean existsByNickname(String nickname);
    Optional<UserInfo> findByUser(User user);
}
