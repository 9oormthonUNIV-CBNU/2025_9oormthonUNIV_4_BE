package goormthon_group4.backend.domain.member.repository;

import goormthon_group4.backend.domain.member.entity.Member;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByTeamAndUser(Team team, User user);
}