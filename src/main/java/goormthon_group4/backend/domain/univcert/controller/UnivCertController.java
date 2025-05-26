package goormthon_group4.backend.domain.univcert.controller;

import goormthon_group4.backend.domain.univcert.dto.UnivCodeRequestDTO;
import goormthon_group4.backend.domain.univcert.dto.UnivRequestDTO;
import goormthon_group4.backend.domain.univcert.service.UnivCertService;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.global.utils.AuthenticatedUserUtils;
import goormthon_group4.backend.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/univcert")
public class UnivCertController {
    private final UnivCertService univCertService;
    private final AuthenticatedUserUtils authenticatedUserUtils;

    // 학교 인증 요청
    @PostMapping
    public ResponseEntity<String> univCertify(@RequestBody UnivRequestDTO dto) throws IOException {
        User user = SecurityUtils.getCurrentUser();
        String result = univCertService.univCertify(dto, user);
        return ResponseEntity.ok(result);
    }

    // 인증 코드 확인
    @PostMapping("/code")
    public ResponseEntity<String> verifyUnivCode(@RequestBody UnivCodeRequestDTO dto) throws IOException {
        User user = SecurityUtils.getCurrentUser();
        return ResponseEntity.ok(univCertService.univCertifyCode(dto, user));
    }
}
