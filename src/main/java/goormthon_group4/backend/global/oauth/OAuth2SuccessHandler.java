package goormthon_group4.backend.global.oauth;

import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.Jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        System.out.println("OAuth2SuccessHandler 호출됨");

        // OAuth2User에서 이메일 추출
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oauth2User.getAttribute("email");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "이메일 정보가 없습니다.");
            return;
        }

        System.out.println("가져온 email: " + email);

        // DB에서 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // JWT 토큰 생성
        String token = jwtProvider.createToken(user.getId());

        // 리다이렉트: 프론트가 없으니 login-success로
        response.sendRedirect("/login-success?token=" + token);
    }
}