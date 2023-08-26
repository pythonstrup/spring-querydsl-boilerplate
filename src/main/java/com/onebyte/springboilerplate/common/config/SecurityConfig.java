package com.onebyte.springboilerplate.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebyte.springboilerplate.common.service.auth.session.JsonSessionAuthenticationFilter;
import com.onebyte.springboilerplate.common.service.auth.session.SessionAuthenticationFailureHandler;
import com.onebyte.springboilerplate.common.service.auth.session.SessionAuthenticationManager;
import com.onebyte.springboilerplate.common.service.auth.session.SessionAuthenticationSuccessHandler;
import com.onebyte.springboilerplate.common.service.oauth.OAuth2UserService;
import com.onebyte.springboilerplate.common.service.oauth.OAuthLoginFailureHandler;
import com.onebyte.springboilerplate.common.service.oauth.OAuthLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

//@EnableWebSecurity(debug = true) // 디버그 모드
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final OAuth2UserService oAuth2UserService;
  private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
  private  final OAuthLoginFailureHandler oAuthLoginFailureHandler;
  private final SessionAuthenticationSuccessHandler sessionAuthenticationSuccessHandler;
  private final SessionAuthenticationFailureHandler sessionAuthenticationFailureHandler;
  private final SessionAuthenticationManager sessionAuthenticationManager;
  private final ObjectMapper objectMapper;


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)

        .addFilterBefore(
            sessionAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class
        )

        // authorization
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers("/oauth2/**", "/login/**").permitAll()
            .requestMatchers("/api/v1/users/**", "/api/v1/auth/**").permitAll()
            .anyRequest().authenticated()
        )

        // OAuth2
        .oauth2Login(oauth -> oauth
            .authorizationEndpoint(authorization -> authorization
                .baseUri("/login/oauth2/authorization")
            )
            .redirectionEndpoint(redirection -> redirection
                .baseUri("/login/oauth2/code/*")
            )
            .userInfoEndpoint(userInfo -> userInfo
                .userService(oAuth2UserService)
            )
            .successHandler(oAuthLoginSuccessHandler)
            .failureHandler(oAuthLoginFailureHandler)
        );
    return http.build();
  }

  @Bean
  public JsonSessionAuthenticationFilter sessionAuthenticationFilter() {
    JsonSessionAuthenticationFilter filter = new JsonSessionAuthenticationFilter(
        sessionAuthenticationSuccessHandler,
        sessionAuthenticationFailureHandler,
        sessionAuthenticationManager,
        objectMapper
    );

    HttpSessionSecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
    filter.setSecurityContextRepository(contextRepository);
    return filter;
  }
}
