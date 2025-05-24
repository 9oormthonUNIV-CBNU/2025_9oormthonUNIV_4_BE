package goormthon_group4.backend.global.utils;

import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.Jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserUtils {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public User getCurrentUser(String bearerToken) {
        String token = extractToken(bearerToken);
        Long userId = jwtProvider.getUserId(token);

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    private String extractToken(String bearerToken) {
        return bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
    }
}
