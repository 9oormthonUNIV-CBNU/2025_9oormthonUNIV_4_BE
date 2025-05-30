package goormthon_group4.backend.domain.team.dto.response;

import goormthon_group4.backend.domain.team.entity.TeamStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyTeamResponse {
  private Long id;
  private TeamStatus status;
  private Integer maxUserCount;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private String title;
  private String content;
  private String projectTitle;
}
