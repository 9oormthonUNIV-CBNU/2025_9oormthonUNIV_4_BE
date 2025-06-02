package goormthon_group4.backend.domain.team.dto.request;

import goormthon_group4.backend.domain.team.entity.TeamStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TeamUpdateOnlyStatusRequest {
  @NotNull
  private TeamStatus status;
}
