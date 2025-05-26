package goormthon_group4.backend.global.config;

<<<<<<< HEAD

=======
import goormthon_group4.backend.global.oauth.GoogleOauth2LoginSuccess;
import goormthon_group4.backend.global.oauth.JwtTokenFilter;
>>>>>>> 5648c99 (feat : 구글 oauth 로그인 구현, JWT 인증 완료)
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final GoogleOauth2LoginSuccess googleOauth2LoginSuccess;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, GoogleOauth2LoginSuccess googleOauth2LoginSuccess) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.googleOauth2LoginSuccess = googleOauth2LoginSuccess;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(configurationSource()))

                // CSRF 비활성화 (JWT 기반 인증이므로 필요 없음)
                .csrf(AbstractHttpConfigurer::disable)

                // STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
<<<<<<< HEAD
                        .requestMatchers("/", "/login", "/oauth/**", "/kakao-login-button.png", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
=======
                        // 인증 없이 접근 가능
                        .requestMatchers(
                                "/",
                                "/login",
                                "/oauth2/**"
                        ).permitAll()
                        .requestMatchers("/api/test/protected").authenticated()
                        // 그 외 모든 요청 인증 필요
>>>>>>> 5648c99 (feat : 구글 oauth 로그인 구현, JWT 인증 완료)
                        .anyRequest().authenticated()
                )
                // OAuth2 로그인 설정
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // 커스텀 로그인 페이지 (프론트와 연결해야함)
                        .successHandler(googleOauth2LoginSuccess)
                )
                // JWT 필터 등록
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
                // oauth 로그인이 성공했을 경우 실행할 클래스 정의
                // .oauth2Login(o -> o.successHandler(googleOauth2LoginSuccess));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*")); //모든 HTTP 메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); //모든 헤더값 허용
        configuration.setAllowCredentials(true); //자격증명허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 url패턴에 대해서 cors 허용 설정
        source.registerCorsConfiguration("/**", configuration);
        return  source;
    }

}
