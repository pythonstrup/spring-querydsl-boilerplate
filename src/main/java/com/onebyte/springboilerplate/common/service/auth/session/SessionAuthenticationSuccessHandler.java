package com.onebyte.springboilerplate.common.service.auth.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebyte.springboilerplate.common.dto.ApiResponse;
import com.onebyte.springboilerplate.common.dto.auth.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException {

    HttpSession session = request.getSession();
    session.setMaxInactiveInterval(60 * 30);

    response.setHeader("content-type", "application/json");
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
