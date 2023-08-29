package com.onebyte.springboilerplate.common.dto.auth;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails, Serializable {

  public CustomUserDetails(String username, String password, Collection<GrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  private final String username;
  private final String password;
  private final Collection<GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  /**
   * 계정 만료되지 않았는지 여부
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * 계정이 잠겨있지 않는지 여부
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * 비밀번호 만료되지 않았는지 여부
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * 사용자 활성화 여부
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}
