package goormthon_group4.backend.domain.application.controller;

import goormthon_group4.backend.domain.application.dto.request.ApplicationRequestDto;
import goormthon_group4.backend.domain.application.dto.request.ApplicationStatusUpdateRequest;
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

import java.util.List;

@Tag(name = "팀 지원 API", description = "지원서 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/teams")
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping(value = "/{teamId}/applications", consumes = "multipart/form-data")
    @Operation(summary = "팀에 지원서 제출", description = "지정된 팀에 지원서를 제출")
    public ApiResponse<ApplicationResponseDto> submitApplication(
            @PathVariable Long teamId,
            @RequestPart("application") ApplicationRequestDto applicationRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ApplicationResponseDto responseDto = applicationService.submitApplication(teamId, applicationRequestDto, customUserDetails, multipartFile);
        return ApiResponse.success(responseDto);
    }

    @GetMapping("/{teamId}/applications/{userId}")
    @Operation(summary = "팀장의 특정 지원서 조회", description = "팀장만 접근 가능")
    public ApiResponse<ApplicationResponseDto> getApplication(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.success(applicationService.getApplication(teamId, userId, customUserDetails));
    }

    @GetMapping("/{teamId}/applications")
    @Operation(summary = "지원서 전체 조회", description = "팀장이 해당 팀에 제출된 모든 지원서를 조회")
    public ApiResponse<List<ApplicationResponseDto>> getApplications(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ApplicationResponseDto> responses = applicationService.getApplicationsByTeamId(teamId, customUserDetails);
        return ApiResponse.success(responses);
    }

    @PatchMapping("/{teamId}/applications/{userId}/status")
    @Operation(summary = "지원서 상태 변경", description = "팀장이 지원자의 지원 상태 변경")
    public ApiResponse<ApplicationStatusUpdateRequest> updateApplicationStatus(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            @RequestBody ApplicationStatusUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ApplicationStatusUpdateRequest updated = applicationService.updateApplication(teamId, userId, request, customUserDetails);
        return ApiResponse.success(updated);
    }
}
