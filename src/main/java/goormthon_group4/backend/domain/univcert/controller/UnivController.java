package goormthon_group4.backend.domain.univcert.controller;

import goormthon_group4.backend.domain.univcert.dto.UnivCodeRequestDto;
import goormthon_group4.backend.domain.univcert.dto.UnivRequestDto;
import goormthon_group4.backend.domain.univcert.service.UnivService;
import goormthon_group4.backend.domain.user.entity.User;
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
@Tag(name = "대학 인증 api", description = "대학 인증 api")

public class UnivController {
    private final UnivService univService;

    @Operation(description = "학교 인증")
    @PostMapping
    public ResponseEntity<String> univCertify(@RequestBody UnivRequestDto univRequestDto,
                                              @AuthenticationPrincipal User user) throws IOException {
        return ResponseEntity.ok(univService.univCertify(univRequestDto, user));
    }

    @Operation(description = "학교 인증")
    @PostMapping("/code")
    public ResponseEntity<String> univCertifyCode(@RequestBody UnivCodeRequestDto univCodeRequestDto,
                                                  @AuthenticationPrincipal User user) throws IOException {
        return ResponseEntity.ok(univService.univCertifyCode(univCodeRequestDto, user));
    }
}
