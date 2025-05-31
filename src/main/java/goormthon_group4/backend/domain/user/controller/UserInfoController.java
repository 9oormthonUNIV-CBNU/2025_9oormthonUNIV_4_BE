package goormthon_group4.backend.domain.user.controller;

import goormthon_group4.backend.domain.user.dto.request.UserInfoRequestDto;
import goormthon_group4.backend.domain.user.dto.response.MypageFullResponseDto;
import goormthon_group4.backend.domain.user.dto.response.UserInfoResponseDto;
import goormthon_group4.backend.domain.user.service.UserInfoService;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userinfo")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @Operation(summary = "유저 정보 등록", description = "UserInfo 최초 저장")
    @PostMapping
    public ApiResponse<String> createUserInfo(@RequestBody UserInfoRequestDto userInfoRequestDto,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userInfoService.saveUserInfo(userInfoRequestDto, customUserDetails);
        return ApiResponse.success("UserInfo 저장 완료");
    }

    @Operation(summary = "유저 정보 조회", description = "현재 로그인한 유저의 UserInfo 반환")
    @GetMapping
    public ApiResponse<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.success(userInfoService.getUserInfo(customUserDetails));
    }

    @Operation(summary = "유저 정보 수정", description = "UserInfo 업데이트")
    @PutMapping
    public ApiResponse<String> updateUserInfo(@RequestBody UserInfoRequestDto userInfoRequestDto,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userInfoService.updateUserInfo(userInfoRequestDto, customUserDetails);
        return ApiResponse.success("UserInfo 수정 완료");
    }

    @GetMapping("/mypage")
    public ApiResponse<MypageFullResponseDto> getMypage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.success(userInfoService.getMypage(userDetails.getUser()));
    }}
