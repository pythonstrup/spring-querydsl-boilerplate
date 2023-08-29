package com.onebyte.springboilerplate.common.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

@Slf4j
public class JsonJwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER
      = new AntPathRequestMatcher("/api/v1/auth/jwt/login", HttpMethod.POST.name());
  private final ObjectMapper objectMapper;

  public JsonJwtAuthenticationFilter(
      JwtAuthenticationSuccessHandler successHandler,
      JwtAuthenticationFailureHandler failureHandler,
      AuthenticationManager authenticationManager,
      ObjectMapper objectMapper
  ) {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    setAuthenticationSuccessHandler(successHandler);
    setAuthenticationFailureHandler(failureHandler);

    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    log.info("Jwt Login Request!!");
    if (!request.getMethod().equals(HttpMethod.POST.name()) || !request.getContentType().equals("application/json")) {
      throw new AuthenticationServiceException("Authentication method not supported: " +  request.getMethod());
    }

    String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

    Map<String, String> map = objectMapper.readValue(messageBody, Map.class);
    String username = map.get("username");
    String password = map.get("password");
    if (username == null || password == null) {
      throw new AuthenticationServiceException("Data is Miss");
    }

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
    return this.getAuthenticationManager().authenticate(authRequest);
  }
}
