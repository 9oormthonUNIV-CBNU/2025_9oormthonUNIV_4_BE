package goormthon_group4.backend.domain.team.dto.request;

import goormthon_group4.backend.domain.team.entity.TeamStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamUpdateRequest {
  @NotNull
  private Integer maxUserCount;

  @NotNull
  private LocalDateTime startAt;

  @NotNull
  private LocalDateTime endAt;

  @NotBlank
  private String title;

  @NotBlank
  private String content;

  @NotNull
  private TeamStatus status;

  private String fileUrl;  // 선택적

}
