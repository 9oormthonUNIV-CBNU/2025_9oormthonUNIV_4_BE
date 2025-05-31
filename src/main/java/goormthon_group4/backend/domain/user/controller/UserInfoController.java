package goormthon_group4.backend.domain.user.controller;

import goormthon_group4.backend.domain.user.dto.UserInfoRequestDto;
import goormthon_group4.backend.domain.user.dto.UserInfoResponseDto;
import goormthon_group4.backend.domain.user.service.UserInfoService;
import goormthon_group4.backend.global.auth.CustomUserDetails;
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
        return ResponseEntity.ok("UserInfo 수정 완료;");
    }
}
