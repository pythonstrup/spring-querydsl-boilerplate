package com.onebyte.springboilerplate.common.service.auth;

import com.onebyte.springboilerplate.domain.dto.auth.CustomUserDetails;
import com.onebyte.springboilerplate.domain.entity.User;
import com.onebyte.springboilerplate.domain.exception.NotFoundException;
import com.onebyte.springboilerplate.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByUsername(username).orElseThrow(NotFoundException::new);
    CustomUserDetails customUserDetails
        = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getEmail());

    return customUserDetails;
  }
}
