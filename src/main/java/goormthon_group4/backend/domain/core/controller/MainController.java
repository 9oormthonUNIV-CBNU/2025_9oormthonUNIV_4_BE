package goormthon_group4.backend.domain.core.controller;

import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class MainController {

  // ✅ DTO 유효성 검증 (@RequestBody + @Valid)
  @PostMapping("/validate-body")
  public ApiResponse<String> validateBody(@RequestBody @Validated TestRequest request) {
    return ApiResponse.success("파라미 터가 유효합니다.");
  }

  // ✅ RequestParam 검증
  @GetMapping("/validate-param")
  public ApiResponse<String> validateParam(@RequestParam @Min(value = 1, message = "id는 1 이상이어야 합니다.") int id) {
    return ApiResponse.success( "파라미터가 유효합니다.");
  }

  // ✅ 커스텀 예외 발생
  @GetMapping("/custom-error")
  public ApiResponse<Void> throwCustomError() {
    throw new CustomException(ErrorCode.USER_NOT_FOUND);
  }

  // ✅ 일반 예외 발생 (NullPointer 등)
  @GetMapping("/general-error")
  public ApiResponse<Void> throwException() {
    throw new NullPointerException("의도적인 NPE 발생");
  }

  // ✅ 정상 응답
  @GetMapping("/success")
  public ApiResponse<String> success() {
    return ApiResponse.success( "정상 응답입니다.");
  }

  // ✅ Request DTO
  @Data
  public static class TestRequest {
    @NotBlank(message = "name은 필수입니다.")
    private String name;

    @Size(min = 2, message = "description은 최소 2자 이상이어야 합니다.")
    private String description;
  }



}