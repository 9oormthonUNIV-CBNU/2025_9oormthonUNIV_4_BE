package goormthon_group4.backend.global.utils;

import goormthon_group4.backend.domain.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("인증된 사용자가 없습니다.");
        }

        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("인증된 사용자가 없습니다.");
        }

        return (User) authentication.getPrincipal();
    }
}