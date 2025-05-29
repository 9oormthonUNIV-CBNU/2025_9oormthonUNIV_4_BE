package goormthon_group4.backend.domain.team.controller;

import goormthon_group4.backend.domain.team.dto.request.TeamCreateRequest;
import goormthon_group4.backend.domain.team.dto.response.TeamCreateResponse;
import goormthon_group4.backend.domain.team.service.TeamService;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "팀 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/teams")
public class TeamController {

  private final TeamService teamService;

  @Operation(summary = "팀 생성하기", description = "팀을 생성 합니다.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "팀 생성 성공"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음")
  })
  public ApiResponse<TeamCreateResponse> createTeam(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody TeamCreateRequest request) {
    TeamCreateResponse responseDto = teamService.create(userDetails.getUser().getId(), request);
    return ApiResponse.success(responseDto);
  }
}
