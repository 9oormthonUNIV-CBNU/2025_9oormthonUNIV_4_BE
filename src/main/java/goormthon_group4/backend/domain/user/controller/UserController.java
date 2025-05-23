package goormthon_group4.backend.domain.user.controller;

import goormthon_group4.backend.domain.user.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/me")
    public String getMyInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            return "로그인 한 ID : " + user.getEmail();
        }

        return "로그인되지 않았습니다.";
    }
}
