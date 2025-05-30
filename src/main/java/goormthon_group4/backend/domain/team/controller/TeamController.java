package goormthon_group4.backend.domain.team.controller;

import goormthon_group4.backend.domain.team.dto.request.TeamCreateRequest;
import goormthon_group4.backend.domain.team.dto.request.TeamUpdateRequest;
import goormthon_group4.backend.domain.team.dto.response.TeamCreateResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamDetailResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamUpdateResponse;
import goormthon_group4.backend.domain.team.service.TeamService;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.Getter;
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
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "팀 생성 실패")
  })
  @PostMapping("/create")
  public ApiResponse<TeamCreateResponse> create(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody TeamCreateRequest request) {
    TeamCreateResponse responseDto = teamService.create(userDetails.getUser().getId(), request);
    return ApiResponse.success(responseDto);
  }

  @Operation(summary = "팀 업데이트 하기", description = "팀을 업데이트 합니다.")
  @PutMapping("/{id}")
  public ApiResponse<TeamUpdateResponse> update(
      @PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody TeamUpdateRequest request
  ){
    TeamUpdateResponse responseDto = teamService.update(id,userDetails.getUser().getId(),request);
    return ApiResponse.success(responseDto);
  }


  @Operation(summary = "팀 삭제하기", description = "팀을 삭제 합니다.")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id
  ){
    teamService.delete(userDetails.getUser().getId(), id);
    return ApiResponse.success(null);
  }

  @Operation(summary = "팀들을 조회하기", description = "프로젝트에 딸린 모든 팀을 가져옵니다.")
  @GetMapping("/projects/{projectId}")
  public ApiResponse<List<TeamResponse>> getTeamsByProjectId(@PathVariable Long projectId) {
    List<TeamResponse> teams = teamService.getTeamsByProjectId(projectId);
    return ApiResponse.success(teams);
  }

  @Operation(summary = "팀 상세 조회하기", description = "팀 상세 정보를 가져옵니다.")
  @GetMapping("/{id}")
  public ApiResponse<TeamDetailResponse> getTeamById(@PathVariable Long id) {
    return ApiResponse.success(teamService.getTeamDetail(id));
  }






}
