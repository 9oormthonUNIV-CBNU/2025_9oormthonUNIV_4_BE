package goormthon_group4.backend.domain.user.controller;

import goormthon_group4.backend.domain.user.dto.request.UserInfoRequestDTO;
import goormthon_group4.backend.domain.user.dto.request.UserInfoResponseDTO;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import goormthon_group4.backend.domain.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userinfo")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PostMapping
    public ResponseEntity<String> createUserInfo(@RequestBody UserInfoRequestDTO dto,
                                         @RequestHeader("Authorization") String bearerToken) {
        userInfoService.saveUserInfo(dto, bearerToken);
        return ResponseEntity.ok("UserInfo 저장 완료");
    }

    @GetMapping
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(@RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok(userInfoService.getUserInfo(bearerToken));
    }

    @PutMapping
    public ResponseEntity<String> updateUserInfo(@RequestBody UserInfoRequestDTO dto,
                                                 @RequestHeader("Authorization") String bearerToken) {
        userInfoService.updateUserInfo(dto, bearerToken);
        return ResponseEntity.ok("UserInfo 수정 완료;");
    }
}
