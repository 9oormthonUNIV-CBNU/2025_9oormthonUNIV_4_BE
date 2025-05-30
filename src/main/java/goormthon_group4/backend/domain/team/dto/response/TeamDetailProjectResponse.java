package goormthon_group4.backend.domain.team.dto.response;

import goormthon_group4.backend.domain.project.entity.Project;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TeamDetailProjectResponse {
  private Long id;
  private LocalDate startAt;
  private LocalDate endAt;
  private String imageUrl;
  private String companyName;
  private String title;

  public static TeamDetailProjectResponse from(Project project) {
    return TeamDetailProjectResponse.builder()
        .id(project.getId())
        .startAt(project.getStartAt())
        .endAt(project.getEndAt())
        .imageUrl(project.getImageUrl())
        .companyName(project.getCompanyName())
        .title(project.getTitle())
        .build();
  }
}
