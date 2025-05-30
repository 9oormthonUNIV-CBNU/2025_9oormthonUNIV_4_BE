package goormthon_group4.backend.domain.tool_link.controller;

import goormthon_group4.backend.domain.tool_link.dto.response.ToolLinkResponse;
import goormthon_group4.backend.domain.tool_link.service.ToolLinkService;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "협업 링크 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tool-links")
public class ToolLinkController {
  private final ToolLinkService toolLinkService;

  @Operation(summary = "협업링크 조회", description = "협업링크들을 조회합니다.")
  @GetMapping
  public ApiResponse<Page<ToolLinkResponse>> getToolLinks(
      @RequestParam(defaultValue = "1") int page
  ) {
    int pageIndex = Math.max(0, page - 1);
    Page<ToolLinkResponse> toolLinks = toolLinkService.getToolLinks(pageIndex);
    return ApiResponse.success(toolLinks);
  }

}
