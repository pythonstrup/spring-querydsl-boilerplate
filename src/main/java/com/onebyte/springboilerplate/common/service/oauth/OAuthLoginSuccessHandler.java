package com.onebyte.springboilerplate.common.service.oauth;

import com.onebyte.springboilerplate.domain.dto.user.UserDto;
import com.onebyte.springboilerplate.domain.dto.user.UserSignInRequest;
import com.onebyte.springboilerplate.domain.entity.User;
import com.onebyte.springboilerplate.domain.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Value("${secret.kakao-secret}")
  private String kakaoSecret;

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

    Optional<User> user = userService.findUserByUsername(email);
    UserDto result = null;

    if (user.isEmpty()) {
      UserSignInRequest signInUser = UserSignInRequest.builder()
          .username(email)
          .email(email)
          .password(kakaoSecret)
          .build();
      result = userService.save(signInUser);
    } else {
      result = UserDto.toDto(user.get());
    }

    HttpSession session = request.getSession();
    session.setAttribute("user", result);

    super.onAuthenticationSuccess(request, response, authentication);
  }
}
