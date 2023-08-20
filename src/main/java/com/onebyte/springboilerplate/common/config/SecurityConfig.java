package com.onebyte.springboilerplate.common.config;

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

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)

        // authorization
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers("/oauth2/**", "/login/**").permitAll()
            .anyRequest().authenticated()
        )
        // OAuth2
        .oauth2Login(oauth -> oauth
            .defaultSuccessUrl("/")
        );
    return http.build();
  }
}
