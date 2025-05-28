package goormthon_group4.backend.domain.user.repository;

import goormthon_group4.backend.domain.user.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;


public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    boolean existsByNickname(String nickname);
}
