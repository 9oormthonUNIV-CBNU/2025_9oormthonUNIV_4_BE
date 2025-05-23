package goormthon_group4.backend.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (API용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/projects/**").permitAll()  // ✅ 이 경로는 로그인 없이 접근 허용
                        .anyRequest().authenticated()                     // 나머지는 인증 필요
                )
                .oauth2Login(oauth2 -> {}); // OAuth2 로그인 기본 설정

        return http.build();
    }
}

