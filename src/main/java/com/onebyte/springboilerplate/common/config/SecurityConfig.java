package com.onebyte.springboilerplate.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebyte.springboilerplate.common.service.auth.session.JsonSessionAuthenticationFilter;
import com.onebyte.springboilerplate.common.service.auth.session.SessionAuthenticationFailureHandler;
import com.onebyte.springboilerplate.common.service.auth.session.SessionAuthenticationManager;
import com.onebyte.springboilerplate.common.service.auth.session.SessionAuthenticationSuccessHandler;
import com.onebyte.springboilerplate.common.service.jwt.JsonJwtAuthenticationFilter;
import com.onebyte.springboilerplate.common.service.jwt.JwtAuthenticationFailureHandler;
import com.onebyte.springboilerplate.common.service.jwt.JwtAuthenticationFilter;
import com.onebyte.springboilerplate.common.service.jwt.JwtAuthenticationManager;
import com.onebyte.springboilerplate.common.service.jwt.JwtAuthenticationSuccessHandler;
import com.onebyte.springboilerplate.common.service.jwt.TokenProvider;
import com.onebyte.springboilerplate.common.service.oauth.OAuth2UserService;
import com.onebyte.springboilerplate.common.service.oauth.OAuthLoginFailureHandler;
import com.onebyte.springboilerplate.common.service.oauth.OAuthLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//@EnableWebSecurity(debug = true) // 디버그 모드
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final OAuth2UserService oAuth2UserService;
  private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
  private final OAuthLoginFailureHandler oAuthLoginFailureHandler;
  private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
  private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
  private final JwtAuthenticationManager jwtAuthenticationManager;
  private final TokenProvider tokenProvider;
  private final ObjectMapper objectMapper;


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())

        /* Sessions
        .addFilterBefore(
            sessionAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class
        )
        */
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .addFilterBefore(
            new JwtAuthenticationFilter(tokenProvider),
            UsernamePasswordAuthenticationFilter.class
        )
        .addFilterBefore(
            jsonJwtAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class
        )

        // authorization
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers("/oauth2/**", "/login/**").permitAll()
            .requestMatchers("/api/v1/auth/**").permitAll()
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
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.addAllowedOriginPattern("*");
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * Jwt Auth
   */
  @Bean
  public JsonJwtAuthenticationFilter jsonJwtAuthenticationFilter() {
    JsonJwtAuthenticationFilter filter = new JsonJwtAuthenticationFilter(
        jwtAuthenticationSuccessHandler,
        jwtAuthenticationFailureHandler,
        jwtAuthenticationManager,
        objectMapper
    );

    return filter;
  }


  /**
   * Session Auth
   */
  private final SessionAuthenticationSuccessHandler sessionAuthenticationSuccessHandler;
  private final SessionAuthenticationFailureHandler sessionAuthenticationFailureHandler;
  private final SessionAuthenticationManager sessionAuthenticationManager;
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
