package goormthon_group4.backend.domain.user.controller;

import goormthon_group4.backend.domain.user.dto.request.IntroduceUpdateRequestDto;
import goormthon_group4.backend.domain.user.dto.request.UserInfoRequestDto;
import goormthon_group4.backend.domain.user.dto.response.MypageFullResponseDto;
import goormthon_group4.backend.domain.user.dto.response.UserInfoResponseDto;
import goormthon_group4.backend.domain.user.service.UserInfoService;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userinfo")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PostMapping
    public ResponseEntity<String> createUserInfo(@RequestBody UserInfoRequestDto userInfoRequestDto,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userInfoService.saveUserInfo(userInfoRequestDto, customUserDetails);
        return ResponseEntity.ok("UserInfo 저장 완료");
    }

    @GetMapping
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userInfoService.getUserInfo(customUserDetails));
    }

    @PutMapping
    public ResponseEntity<String> updateUserInfo(@RequestBody UserInfoRequestDto userInfoRequestDto,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userInfoService.updateUserInfo(userInfoRequestDto, customUserDetails);
        return ResponseEntity.ok("UserInfo 수정 완료");
    }

    @GetMapping("/mypage")
    public ApiResponse<MypageFullResponseDto> getMypage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.success(userInfoService.getMypage(userDetails.getUser()));
    }

    @PutMapping("/mypage/introduce")
    public ResponseEntity<String> updateIntroduce(@RequestBody IntroduceUpdateRequestDto dto,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userInfoService.updateIntroduce(dto.getIntroduce(), customUserDetails);
        return ResponseEntity.ok("mypage 내 자기소개 수정 완료");
    }
}
