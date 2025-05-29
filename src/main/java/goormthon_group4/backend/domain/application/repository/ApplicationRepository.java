package goormthon_group4.backend.domain.application.repository;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    // 특정 유저가 특정 팀에 이미 지원했는지 확인
    boolean existsByUserAndTeam(User user, Team team);

    // 특정 팀에 속한 지원서 모두 조회
    List<Application> findAllByTeam(Team team);

    // 특정 유저가 작성한 모든 지원서 조회
    List<Application> findAllByUser(User user);

    // 유저와 팀 기준으로 지원서 단건 조회
    Optional<Application> findByUserAndTeam(User user, Team team);
}
