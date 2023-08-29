package com.onebyte.springboilerplate.common.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebyte.springboilerplate.common.dto.ApiResponse;
import com.onebyte.springboilerplate.common.dto.auth.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final TokenProvider tokenProvider;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException {

    String token = tokenProvider.createToken(authentication);

    response.setHeader("content-type", "application/json");
    response.setHeader("Authorization", "Bearer " + token);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_OK);
    LoginResponse loginResponse = LoginResponse.builder()
        .username((String) authentication.getPrincipal())
        .build();
    ResponseEntity<ApiResponse<LoginResponse>> responseEntity = ResponseEntity.ok(
        new ApiResponse<>(loginResponse, "")
    );
    String result = objectMapper.writeValueAsString(responseEntity.getBody());
    response.getWriter().write(result);
  }
}
