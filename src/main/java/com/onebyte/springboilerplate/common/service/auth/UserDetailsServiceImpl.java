package com.onebyte.springboilerplate.common.service.auth;

import com.onebyte.springboilerplate.common.dto.auth.CustomUserDetails;
import com.onebyte.springboilerplate.domain.entity.User;
import com.onebyte.springboilerplate.domain.exception.NotFoundException;
import com.onebyte.springboilerplate.domain.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    return new CustomUserDetails(user.getUsername(), user.getPassword(), loadUserAuthorities());
  }

  private Collection<GrantedAuthority> loadUserAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ADMIN"));
    return authorities;
  }
}
