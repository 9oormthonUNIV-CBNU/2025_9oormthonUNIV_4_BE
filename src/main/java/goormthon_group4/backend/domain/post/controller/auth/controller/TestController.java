package goormthon_group4.backend.domain.post.controller.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/test/protected")
    public String protectedEndpoint() {
        return "✅ 토큰이 유효하므로 접근 성공!";
    }

}
