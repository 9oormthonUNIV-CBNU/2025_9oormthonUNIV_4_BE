package goormthon_group4.backend.domain.team.controller;

import goormthon_group4.backend.domain.team.dto.request.OutputUploadDto;
import goormthon_group4.backend.domain.team.dto.request.TeamCreateRequest;
import goormthon_group4.backend.domain.team.dto.request.TeamUpdateOnlyStatusRequest;
import goormthon_group4.backend.domain.team.dto.request.TeamUpdateRequest;
import goormthon_group4.backend.domain.team.dto.response.MyTeamResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

  @Operation(summary = "팀 상태만을 변경하기", description = "팀 상태만 변경합니다.")
  @PutMapping("/{id}/status")
  public ApiResponse<TeamUpdateResponse> updateOnlyStatus(
      @PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody TeamUpdateOnlyStatusRequest request
  )
  {
    TeamUpdateResponse responseDto = teamService.updateOnlyStatus(id,userDetails.getUser().getId(),request);
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

  @Operation(summary = "내 팀 가져오기", description = "내 팀을 가져옵니다.")
  @GetMapping("/my")
  public ApiResponse<List<MyTeamResponse>> getMyTeams(
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    return ApiResponse.success(teamService.getTeamsByUserId(userDetails.getUser().getId()));
  }

  @Operation(summary = "최종 산출물 제출", description = "팀에서 최종 산출물을 업로드합니다.")
  @PostMapping(value = "{teamId}/output", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<String> uploadFinalOutput(@PathVariable Long teamId,
                                               @RequestPart("file") MultipartFile file,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
    String fileUrl = teamService.uploadFinalOutput(teamId, file, userDetails.getUser());
    return ApiResponse.success(fileUrl);
  }

  @Operation(summary = "!최종 산출물 제출 하나씩!", description = "팀에서 최종 산출물 하나를 삭제합니다.")
  @DeleteMapping("/{teamId}/outputs/{outputId}")
  public ApiResponse<String> deleteFinalOutput(@PathVariable Long teamId,
                                               @PathVariable Long outputId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
    teamService.deleteOutput(teamId, outputId, userDetails.getUser());
    return ApiResponse.success("산출물이 성공적으로 삭제되었습니다.");
  }

  @Operation(summary = "팀 산출물 전체 삭제", description = "특정 팀의 모든 산출물을 삭제합니다.")
  @DeleteMapping("/{teamId}/outputs")
  public ApiResponse<String> deleteAllOutputsByTeam(@PathVariable Long teamId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
    teamService.deleteAllOutputsByTeam(teamId, userDetails.getUser());
    return ApiResponse.success("해당 팀의 모든 산출물이 삭제되었습니다.");
  }


}
