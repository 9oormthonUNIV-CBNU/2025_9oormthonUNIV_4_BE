package goormthon_group4.backend.domain.member.controller;

import goormthon_group4.backend.domain.member.dto.MemberProfileDto;
import goormthon_group4.backend.domain.member.service.MemberService;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{userId}/profile")
    public ApiResponse<MemberProfileDto> getMemberProfile(@PathVariable Long userId) {
        return ApiResponse.success(memberService.getMemberProfile(userId));
    }


    @DeleteMapping("/{teamId}/members/{userId}")
    public ApiResponse<Void> removeTeamMember(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            @AuthenticationPrincipal Long loginUserId) {

        memberService.kickMemberFromTeam(teamId, userId, loginUserId);
        return ApiResponse.success(null);
    }
}
