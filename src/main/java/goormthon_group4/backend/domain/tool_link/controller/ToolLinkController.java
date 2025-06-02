package goormthon_group4.backend.domain.tool_link.controller;

import goormthon_group4.backend.domain.tool_link.dto.request.ToolLinkCreateRequest;
import goormthon_group4.backend.domain.tool_link.dto.request.ToolLinkDeleteRequest;
import goormthon_group4.backend.domain.tool_link.dto.response.ToolLinkResponse;
import goormthon_group4.backend.domain.tool_link.service.ToolLinkService;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "협업 링크 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tool-links")
public class ToolLinkController {
  private final ToolLinkService toolLinkService;

  @Operation(summary = "협업링크 조회", description = "협업링크들을 조회합니다.(페이지 네이션)")
  @GetMapping("/teams/{teamId}")
  public ApiResponse<Page<ToolLinkResponse>> getToolLinksByTeamId(
      @PathVariable Long teamId,
      @RequestParam(defaultValue = "1") int page
  ) {
    int pageIndex = Math.max(0, page - 1);
    Page<ToolLinkResponse> toolLinks = toolLinkService.getToolLinksByTeamId(teamId,pageIndex);
    return ApiResponse.success(toolLinks);
  }

  @Operation(summary = "협업링크 조회2", description = "협업링크들을 조회합니다.")
  @GetMapping("/all/teams/{teamId}")
  public ApiResponse<List<ToolLinkResponse>> getAllToolLinksByTeamId(
      @PathVariable Long teamId
  ) {
    return ApiResponse.success(toolLinkService.getAllToolLinksByTeamId(teamId));
  }

  @Operation(summary = "협업링크 생성", description = "협업링크를 생성합니다.")
  @PostMapping("/{teamId}")
  public ApiResponse<ToolLinkResponse> createToolLink(
      @RequestBody ToolLinkCreateRequest toolLinkCreateRequest,
      @PathVariable Long teamId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    return ApiResponse.success(toolLinkService.createToolLink(
        teamId,
        toolLinkCreateRequest,
        customUserDetails.getUser().getId()
    ));
  }


  @Operation(summary = "협업링크 다중 삭제", description = "여러 개의 협업링크를 삭제합니다.")
  @DeleteMapping("/{teamId}")
  public ApiResponse<Void> deleteToolLinks(@RequestBody ToolLinkDeleteRequest request, @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId) {
    toolLinkService.deleteToolLinks(request.getIds(), userDetails.getUser().getId(), teamId);
    return ApiResponse.success(null);
  }







}
