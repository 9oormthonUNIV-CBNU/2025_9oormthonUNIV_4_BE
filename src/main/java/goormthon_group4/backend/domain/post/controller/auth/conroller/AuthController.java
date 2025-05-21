package goormthon_group4.backend.domain.post.controller.auth.conroller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login-success")
    public String loginSuccessPage(Model model) {
        model.addAttribute("message", "카카오 로그인에 성공했습니다.");
        return "login-success";
    }
}
