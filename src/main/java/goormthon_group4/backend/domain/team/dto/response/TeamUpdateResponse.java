package goormthon_group4.backend.domain.team.dto.response;

import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.team.entity.TeamStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TeamUpdateResponse {
  private Long id;
  private TeamStatus status;
  private Integer maxUserCount;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private String title;
  private String content;
  private String fileUrl;
  private Long leaderId;
  @Builder
  public TeamUpdateResponse(Team team) {
    this.id = team.getId();
    this.status = team.getStatus();
    this.maxUserCount = team.getMaxUserCount();
    this.startAt = team.getStartAt();
    this.endAt = team.getEndAt();
    this.title = team.getTitle();
    this.content = team.getContent();
    if (team.getFileUrl() != null) {
      this.fileUrl = team.getFileUrl();
    }
    this.leaderId = team.getLeader().getId();
  }

}
