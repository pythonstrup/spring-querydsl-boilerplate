package com.onebyte.springboilerplate.domain.dto.user;

import com.onebyte.springboilerplate.domain.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class UserSignInRequest {
  private String username;
  private String password;
  private String email;
  private Integer age;

  public User toEntity() {
    return User.builder()
        .username(username)
        .password(password)
        .email(email)
        .age(age)
        .build();
  }

  public void encodePassword(String encodedPassword) {
    password = encodedPassword;
  }
}
