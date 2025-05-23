package goormthon_group4.backend.global.Jwt;

import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    /**
     * 서버에 들어오는 HTTP 요청마다 실행되는 필터 메서드
     * Authorization 헤더에 JWT를 검ㅁ증하고, 인증 정보를 SecurityContext에 등록
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        // 요청 헤더에서 Authorization 값 가져옴
        String authHeader = request.getHeader("Authorization");

        // 헤더 존재 && 'Bearer '로 시작할 경우만 처리
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            // "Bearer " 제외하고 실제 토큰 값 추출
            String token = authHeader.substring(7);

            // 토큰 유효성 검사
            if (jwtProvider.validateToken(token)) {

                // 토큰에서 ID 추출
                Long userId = jwtProvider.getUserId(token);

                // DB에서 사용자 정보 조회
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 현재 SecurityContext에 인증 정보 등록 (세션 인증처럼 동작)
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
