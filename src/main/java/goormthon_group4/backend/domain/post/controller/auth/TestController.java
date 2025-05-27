package goormthon_group4.backend.domain.post.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "✅ 토큰이 유효하므로 접근 성공!";
    }

    @GetMapping("/cookie")
    public ResponseEntity<String> cookieEndpoint(@CookieValue("token") String jwttoken) {
        return ResponseEntity.ok("JWT 쿠키 인증 성공" + jwttoken);
    }

}
