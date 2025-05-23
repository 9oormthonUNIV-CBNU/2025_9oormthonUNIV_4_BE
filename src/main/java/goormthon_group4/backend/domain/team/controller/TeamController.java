package goormthon_group4.backend.domain.team.controller;

import goormthon_group4.backend.domain.team.dto.request.TeamCreateRequest;
import goormthon_group4.backend.domain.team.dto.response.TeamCreateResponse;
import goormthon_group4.backend.domain.team.service.TeamService;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@Tag(name = "팀 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/teams")
public class TeamController {

  private final TeamService teamService;

  // 팀 생성
  @PostMapping("/create")
  @Operation(summary = "팀 생성하기", description = "")
  public ApiResponse<TeamCreateResponse> createTeam(
      @RequestBody TeamCreateRequest request) {
    TeamCreateResponse responseDto = teamService.create(request);
    return ApiResponse.success(responseDto);
  }
}
