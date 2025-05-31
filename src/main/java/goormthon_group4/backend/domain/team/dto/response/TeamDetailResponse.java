package goormthon_group4.backend.domain.team.dto.response;

import goormthon_group4.backend.domain.member.dto.MemberResponseDto;
import goormthon_group4.backend.domain.notify.dto.NotifySummaryDto;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.team.entity.TeamStatus;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TeamDetailResponse {
  private Long id;
  private TeamStatus status;
  private Integer maxUserCount;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private String title;
  private String content;
  private String fileUrl;
  private Long leaderId;
  private TeamDetailProjectResponse project;
  private int memberCount;
  private List<NotifySummaryDto> notifies;
  private List<MemberResponseDto> members;

  public static TeamDetailResponse from(Team team, TeamDetailProjectResponse project, int memberCount,List<NotifySummaryDto> notifies, List<MemberResponseDto> members) {
    return TeamDetailResponse.builder()
            .id(team.getId())
            .status(team.getStatus())
            .maxUserCount(team.getMaxUserCount())
            .startAt(team.getStartAt())
            .endAt(team.getEndAt())
            .title(team.getTitle())
            .content(team.getContent())
            .fileUrl(team.getFileUrl())
            .leaderId(team.getLeader().getId())
            .project(project)
            .memberCount(memberCount)
            .notifies(notifies)
            .members(members)
            .build();
  }
}