package goormthon_group4.backend.domain.team.repository;

import goormthon_group4.backend.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
