package goormthon_group4.backend.domain.application.controller;

import goormthon_group4.backend.domain.application.dto.request.ApplicationRequestDto;
import goormthon_group4.backend.domain.application.dto.response.ApplicationResponseDto;
import goormthon_group4.backend.domain.application.service.ApplicationService;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "팀 지원 API", description = "지원서 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "팀 과제 지원서 제출", description = "지정된 팀에 지원서를 제출")
    public ApiResponse<ApplicationResponseDto> submitApplication(
            @RequestPart("application") ApplicationRequestDto applicationRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ApplicationResponseDto responseDto = applicationService.submitApplication(applicationRequestDto, customUserDetails, multipartFile);
        return ApiResponse.success(responseDto);
    }

    @Operation(summary = "팀장의 특정 지원서 조회", description = "팀장만 접근 가능")
    @GetMapping("/{teamId}/{userId}")
    public ApiResponse<ApplicationResponseDto> getApplication(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails cutomUserDetails) {
        return ApiResponse.success(applicationService.getApplication(teamId, userId, cutomUserDetails));
    }
}
