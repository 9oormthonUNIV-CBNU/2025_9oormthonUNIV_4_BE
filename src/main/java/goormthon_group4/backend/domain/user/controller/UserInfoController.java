package goormthon_group4.backend.domain.user.controller;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.application.repository.ApplicationRepository;
import goormthon_group4.backend.domain.user.dto.request.UserInfoRequestDto;
import goormthon_group4.backend.domain.user.dto.response.MypageFullResponseDto;
import goormthon_group4.backend.domain.user.dto.response.UserInfoResponseDto;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.service.UserInfoService;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userinfo")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final ApplicationRepository applicationRepository;

    @PostMapping
    @Operation(summary = "유저 정보 등록", description = "UserInfo 최초 저장")
    public ApiResponse<String> createUserInfo(@RequestBody UserInfoRequestDto userInfoRequestDto,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userInfoService.saveUserInfo(userInfoRequestDto, customUserDetails);
        return ApiResponse.success("UserInfo 저장 완료");
    }

    @GetMapping
    @Operation(summary = "유저 정보 조회", description = "현재 로그인한 유저의 UserInfo 반환")
    public ApiResponse<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.success(userInfoService.getUserInfo(customUserDetails));
    }

    @PutMapping
    @Operation(summary = "유저 정보 수정", description = "UserInfo 업데이트")
    public ApiResponse<String> updateUserInfo(@RequestBody UserInfoRequestDto userInfoRequestDto,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userInfoService.updateUserInfo(userInfoRequestDto, customUserDetails);
        return ApiResponse.success("UserInfo 수정 완료");
    }

    @GetMapping("/mypage")
    @Operation(summary = "마이페이지 조회", description = "현재 로그인한 유저의 마이페이지 정보 반환")
    public ApiResponse<MypageFullResponseDto> getMypage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        // 해당 유저가 지원한 지원서 리스트 조회
        List<Application> applications = applicationRepository.findAllByUser(user);

        // DTO 변환
        MypageFullResponseDto mypageFullResponseDto = MypageFullResponseDto.form(user, applications);

        return ApiResponse.success(mypageFullResponseDto);
    }
}
