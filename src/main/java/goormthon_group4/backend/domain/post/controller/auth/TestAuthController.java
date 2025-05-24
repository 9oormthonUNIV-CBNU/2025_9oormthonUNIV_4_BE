package goormthon_group4.backend.domain.post.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestAuthController {

    @GetMapping("/auth-check")
    public ResponseEntity<String> checkAuth() {
        return ResponseEntity.ok("🔐 인증된 사용자입니다. JWT 필터가 작동했습니다.");
    }
}