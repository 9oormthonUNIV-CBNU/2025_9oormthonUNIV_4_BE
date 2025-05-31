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

        // oauth2 인증된 사용자 프로필 추출
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String openId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String picture = oauth2User.getAttribute("picture"); // 프로필 이미지 URL

        // DB에서 해당 이메일로 가입된 사용자 조회
        Optional<User> optionalUser = userRepository.findByEmail(email);

        boolean isNewUser = false;
        User user;

        if (optionalUser.isPresent()) {
            // 기존 회원이면 그대로 사용
            user = optionalUser.get();
        } else {
            // 신규 회원이면 회원 정보 생성 후 저장
            user = User.builder()
                    .socialId(openId)
                    .email(email)
                    .build();
            userRepository.save(user);
            isNewUser = true;
        }

        // jwt 토큰 생성
        String jwtToken = jwtProvider.createToken(user.getEmail(), user.getRole().toString());
        System.out.println(jwtToken);

        /*
        // jwt 쿠키 저장
        Cookie jwtCookie = new Cookie("token", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/"); // 모든 경로에서 쿠키 사용 가능
        jwtCookie.setMaxAge(3600);   // 유효 시간 (초 단위)
        response.addCookie(jwtCookie);
         */

        // 프론트엔드로 리다이렉트 (회원 여부에 따라 다른 경로)
        if (isNewUser) {
            response.sendRedirect("http://localhost:3000/register?token=" + jwtToken);
        } else {
            response.sendRedirect("http://localhost:3000/?token=" + jwtToken);
        }
    }
}
