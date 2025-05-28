package goormthon_group4.backend.global.oauth;

import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    private final UserRepository userRepository;
    @Value("${jwt.secret}")
    private String secretKey;

    public JwtTokenFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader("Authorization");

        try {
            // 토큰이 없거나 Bearer 형식이 아니면 다음 필터로 진행 (인증 처리 안함)
            if (token == null || !token.startsWith("Bearer ")) {
                chain.doFilter(request, response);
                return;
            }

            // "Bearer " 접두사 제거
            String jwtToken = token.substring(7);

            // secretKey 디코딩하여 HMAC-SHA 키 생성
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

            // JWT 파싱 및 검증
            Jwt<?, Claims> parsedJwt = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwtToken);

            Claims claims = parsedJwt.getPayload();
            String email = claims.getSubject();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다." + email));

            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails, jwtToken, customUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // JWT 파싱 실패나 유효하지 않은 경우
            e.printStackTrace();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("Invalid token");
            return;
        }

        // 인증 여부와 관계없이 다음 필터 진행
        chain.doFilter(request, response);
    }
}