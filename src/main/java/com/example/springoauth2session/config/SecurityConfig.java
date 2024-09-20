package com.example.springoauth2session.config;

import com.example.springoauth2session.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable());

        http
                .formLogin((login) -> login.disable());

        // 기본적으로 Spring Security는 HTTP Basic 인증을 활성화하고, 이는 브라우저가 인증을 요청할 때 사용자 이름과 비밀번호를 포함하여 인증하는 방식
        // Oauth2 로그인을 구현 중이기에, HTTP Basic 인증은 필요 없을 수 있어서 이 부분을 비활성화
        http
                .httpBasic((basic) -> basic.disable());

        http
//                .oauth2Login(Customizer.withDefaults());
                .oauth2Login((oauth) -> oauth
                        .loginPage("/login") // Oauth2에서 제공하는 로그인 페이지가 아닌 커스텀한 로그인 페이지를 띄우기 위해 컨트롤러 경로를 명시.
                        .userInfoEndpoint((userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))));
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

}
