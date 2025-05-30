package goormthon_group4.backend.domain.team.dto.request;

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

  private String fileUrl;  // 선택적

}
