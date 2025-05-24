package goormthon_group4.backend.global.config;

import goormthon_group4.backend.global.Jwt.JwtAuthenticationFilter;
import goormthon_group4.backend.global.oauth.CustomOAuth2UserService;
import goormthon_group4.backend.global.oauth.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter,
                                                   OAuth2SuccessHandler oAuth2SuccessHandler, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        System.out.println("Security Filter Chain 등록됨");
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ✅ 꼭 추가
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/oauth/**", "/kakao-login-button.png").permitAll()
                        .requestMatchers("/test/**", "/v1/univcert/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler) // 로그인 성공 후 리다이렉션 페이지 "/" -> 다른걸로
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
