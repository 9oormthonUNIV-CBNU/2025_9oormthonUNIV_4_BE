package goormthon_group4.backend.domain.core.controller;

import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.ErrorCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class MainController {

  @GetMapping("/auth")
  public String authTest() {
    throw new CustomException(ErrorCode.UNAUTHORIZED);
  }

  @GetMapping("/forbidden")
  public String forbiddenTest() {
    throw new CustomException(ErrorCode.FORBIDDEN);
  }

  @GetMapping("/param")
  public String paramTest(@RequestParam String name) {
    return "Hello " + name;
  }

  @PostMapping("/dto")
  public String dtoTest(@RequestBody @Valid TestRequest request) {
    return "Hello " + request.getName();
  }

  @Getter
  @Setter
  public static class TestRequest {
    @NotBlank(message = "이름은 비어 있을 수 없습니다.")
    private String name;
  }
}