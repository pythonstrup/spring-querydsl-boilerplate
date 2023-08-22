package com.onebyte.springboilerplate.service.oauth;

import com.onebyte.springboilerplate.dto.UserDto;
import com.onebyte.springboilerplate.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final UserService userService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
    String type = token.getAuthorizedClientRegistrationId();

    String email = null;
    if (type.equals("kakao-login")) {
      email = ((Map<String, Object>) token.getPrincipal().getAttribute("kakao_account")).get("email").toString();
    }

    UserDto user = userService.save(UserDto.builder().username(email).build());
    HttpSession session = request.getSession();
    session.setAttribute("user", user);

    super.onAuthenticationSuccess(request, response, authentication);
  }
}
