package goormthon_group4.backend.global.oauth;

import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.Jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class GoogleOauth2LoginSuccess extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public GoogleOauth2LoginSuccess(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // oauth 프로필 추출
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String openId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");

        // 회원가입 여부 확인
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = User.builder()
                    .socialId(openId)
                    .email(email)
                    .build();
            userRepository.save(user);
        }

        // jwt 토큰 생성
        String jwtToken = jwtProvider.createToken(user.getEmail(), user.getRole().toString());
        System.out.println(jwtToken);

        // 1. 클라이언트 redirect 방식으로 토큰 전달
        // response.sendRedirect("http://localhost:8080?token=" + jwtToken);

        // 2. jwt 쿠키 저장
        Cookie jwtCookie = new Cookie("token", jwtToken);
        jwtCookie.setPath("/"); // 모든 경로에서 쿠키 사용 가능
        response.sendRedirect("http://localhost:8080?token=" + jwtCookie.getValue());
    }

}
