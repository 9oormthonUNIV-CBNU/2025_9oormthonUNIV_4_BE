package goormthon_group4.backend.domain.univcert.controller;

import goormthon_group4.backend.domain.univcert.dto.UnivCodeRequestDto;
import goormthon_group4.backend.domain.univcert.dto.UnivRequestDto;
import goormthon_group4.backend.domain.univcert.service.UnivService;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/univcert")

public class UnivController {
    private final UnivService univService;

    @PostMapping
    public ResponseEntity<String> univCertify(@RequestBody UnivRequestDto univRequestDto,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        return ResponseEntity.ok(univService.univCertify(univRequestDto, customUserDetails));
    }

    @PostMapping("/code")
    public ResponseEntity<String> univCertifyCode(@RequestBody UnivCodeRequestDto univCodeRequestDto,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        return ResponseEntity.ok(univService.univCertifyCode(univCodeRequestDto, customUserDetails));
    }

    @PostMapping("/clear")
    public ResponseEntity<String> univClear(@RequestBody UnivRequestDto univRequestDto,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        return ResponseEntity.ok(univService.univClear(univRequestDto, customUserDetails));
    }
}
