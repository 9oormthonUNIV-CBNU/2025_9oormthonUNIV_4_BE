package goormthon_group4.backend.domain.univcert.controller;

import goormthon_group4.backend.domain.univcert.dto.UnivCodeRequestDto;
import goormthon_group4.backend.domain.univcert.dto.UnivRequestDto;
import goormthon_group4.backend.domain.univcert.service.UnivService;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
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

@Tag(name = "대학교 인증 API", description = "UnivCertAPI를 통해 이메일 인증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/univcert")
public class UnivController {

    private final UnivService univService;

    @Operation(summary = "대학교 이메일 인증 요청", description = "유저 학교 이메일로 인증 코드 전송")
    @PostMapping
    public ApiResponse<String> univCertify(@RequestBody UnivRequestDto univRequestDto,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        String result = univService.univCertify(univRequestDto, customUserDetails);
        return ApiResponse.success(result);
    }

    @Operation(summary = "인증 코드 검증", description = "사용자가 받은 인증 코드 검증")
    @PostMapping("/code")
    public ApiResponse<String> univCertifyCode(@RequestBody UnivCodeRequestDto univCodeRequestDto,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        String result = univService.univCertifyCode(univCodeRequestDto, customUserDetails);
        return ApiResponse.success(result);
    }

    @Operation(summary = "인증 정보 초기화", description = "학교 인증 관련 정보 초기화")
    @PostMapping("/clear")
    public ApiResponse<String> univClear(@RequestBody UnivRequestDto univRequestDto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        String result = univService.univClear(univRequestDto, customUserDetails);
        return ApiResponse.success(result);
    }
}