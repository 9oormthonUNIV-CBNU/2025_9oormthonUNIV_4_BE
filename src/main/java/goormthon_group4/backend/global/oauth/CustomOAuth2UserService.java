package goormthon_group4.backend.global.oauth;

import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 카카오 OAuth 로그인 시 사용자 정보를 불러오고 DB에 저장 or 조회하는 서비스
 * 카카오 로그인 시 이메일 기반으로 사용자 정보 저장 or 조회
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 카카오에서 사용자 정보 불러오기
        OAuth2User oauth2User = super.loadUser(userRequest);

        System.out.println("=== Kakao attributes ===");
        System.out.println(oauth2User.getAttributes());

        // 카카오 계정 정보에서 이메일 꺼내기
        Map<String, Object> attributes = oauth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        // DB에 사용자 저장
        userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                        .email(email)
                        .password("")
                        .build()
                ));

        // attributes에 email 직접 넣어줌
        Map<String, Object> customAttributes = new HashMap<>();
        customAttributes.put("email", email);

        // ROLE_USER 권한 부여해서 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "email"
        );
    }
}