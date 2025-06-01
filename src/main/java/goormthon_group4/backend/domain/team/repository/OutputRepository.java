package goormthon_group4.backend.domain.team.repository;

import goormthon_group4.backend.domain.team.entity.Output;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutputRepository extends JpaRepository<Output, Long> {
    List<Output> findAllByTeamId(Long teamId);
}
