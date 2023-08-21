package com.onebyte.springboilerplate.common.config;

import com.onebyte.springboilerplate.service.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity(debug = true) // 디버그 모드
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final OAuth2UserService oAuth2UserService;

  public SecurityConfig(OAuth2UserService oAuth2UserService) {
    this.oAuth2UserService = oAuth2UserService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)

        // authorization
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers("/oauth2/**", "/login/**").permitAll()
            .requestMatchers("/api/v1/users").authenticated()
        )
        // OAuth2
        .oauth2Login(oauth -> oauth
            .authorizationEndpoint(authorization -> authorization
                .baseUri("/login/oauth")
            )
            .redirectionEndpoint(redirection -> redirection
                .baseUri("/login/oauth2/code/*")
            )
            .userInfoEndpoint(userInfo -> userInfo
                .userService(oAuth2UserService)
            )
        );
    return http.build();
  }
}
