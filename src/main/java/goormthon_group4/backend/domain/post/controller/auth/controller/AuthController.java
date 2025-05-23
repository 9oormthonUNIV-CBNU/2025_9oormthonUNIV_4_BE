package goormthon_group4.backend.domain.post.controller.auth.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login-success")
    public String loginSuccessPage(@RequestParam String token, Model model) {
        model.addAttribute("message", "카카오 로그인에 성공했습니다.");
        model.addAttribute("token", token);
        return "login-success";
    }
}