package goormthon_group4.backend.domain.team.repository;

import goormthon_group4.backend.domain.team.dto.response.TeamResponse;
import goormthon_group4.backend.domain.team.entity.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Long> {
  @Query("""
    SELECT new goormthon_group4.backend.domain.team.dto.response.TeamResponse(
        t.id,
        t.status,
        t.maxUserCount,
        t.startAt,
        t.endAt,
        t.title,
        t.content,
        t.project.title,
        SIZE(t.members)
    )
    FROM Team t
    WHERE t.project.id = :projectId
""")
  List<TeamResponse> findTeamResponsesByProjectId(@Param("projectId") Long projectId);
}
