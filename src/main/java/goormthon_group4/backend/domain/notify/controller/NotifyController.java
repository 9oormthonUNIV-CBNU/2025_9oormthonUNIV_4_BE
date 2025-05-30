package goormthon_group4.backend.domain.notify.controller;

import goormthon_group4.backend.domain.notify.dto.NotifyDetailDto;
import goormthon_group4.backend.domain.notify.dto.NotifyRequestDto;
import goormthon_group4.backend.domain.notify.dto.NotifySummaryDto;
import goormthon_group4.backend.domain.notify.dto.NotifyUpdateRequestDto;
import goormthon_group4.backend.domain.notify.service.NotifyService;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams/{teamId}/notifies")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;

    @Operation(summary = "공지사항 등록")
    @PostMapping
    public ApiResponse<Void> createNotify(@PathVariable Long teamId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestBody NotifyRequestDto dto) {
        notifyService.create(teamId, userDetails.getUser(), dto);
        return ApiResponse.success(null);
    }

    @Operation(summary = "공지사항 조회")
    @GetMapping
    public ApiResponse<Page<NotifySummaryDto>> getNoticesByTeam(
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "0") int page) {
        int size = 3;
        return ApiResponse.success(notifyService.getNoticesByTeam(teamId, page, size));
    }

    @Operation(summary = "공지사항 수정")
    @PutMapping("/{notifyId}")
    public ApiResponse<Void> updateNotify(@PathVariable Long teamId,
                                          @PathVariable Long notifyId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestBody NotifyUpdateRequestDto dto) {
        notifyService.update(notifyId, userDetails.getUser().getId(), dto);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{notifyId}")
    @Operation(summary = "공지사항 삭제")
    public ApiResponse<Void> deleteNotify(@PathVariable Long teamId,
                                          @PathVariable Long notifyId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        notifyService.delete(teamId, notifyId, userDetails.getUser().getId());
        return ApiResponse.success(null);
    }

    @Operation(summary = "공지사항 상세 조회")
    @GetMapping("/{notifyId}")
    public ApiResponse<NotifyDetailDto> getNotifyDetail(@PathVariable Long teamId,
                                                        @PathVariable Long notifyId) {
        return ApiResponse.success(notifyService.getDetail(notifyId));
    }

}
