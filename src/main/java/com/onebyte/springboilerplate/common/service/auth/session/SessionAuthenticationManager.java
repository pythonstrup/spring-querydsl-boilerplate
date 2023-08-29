package com.onebyte.springboilerplate.common.service.auth.session;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class SessionAuthenticationManager implements AuthenticationManager {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());

    String requestPassword = authentication.getCredentials().toString();
    if (!passwordEncoder.matches(requestPassword, userDetails.getPassword())) {
      throw new BadCredentialsException("Not Match Password");
    }

    return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
        userDetails.getAuthorities());
  }
}
