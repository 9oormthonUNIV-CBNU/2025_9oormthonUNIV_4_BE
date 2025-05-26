package goormthon_group4.backend.domain.user.controller;

import goormthon_group4.backend.domain.user.dto.UserInfoRequestDto;
import goormthon_group4.backend.domain.user.dto.UserInfoResponseDto;
import goormthon_group4.backend.domain.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userinfo")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PostMapping
    public ResponseEntity<String> createUserInfo(@RequestBody UserInfoRequestDto dto) {
        userInfoService.saveUserInfo(dto);
        return ResponseEntity.ok("UserInfo 저장 완료");
    }

    @GetMapping
    public ResponseEntity<UserInfoResponseDto> getUserInfo() {
        return ResponseEntity.ok(userInfoService.getUserInfo());
    }

    @PutMapping
    public ResponseEntity<String> updateUserInfo(@RequestBody UserInfoRequestDto dto) {
        userInfoService.updateUserInfo(dto);
        return ResponseEntity.ok("UserInfo 수정 완료;");
    }
}
