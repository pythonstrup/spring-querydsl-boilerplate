package com.onebyte.springboilerplate.dto;

import com.onebyte.springboilerplate.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class UserDto {

  private Integer id;
  private String username;
  private Integer age;

  public User toEntity() {
    return User.builder()
        .id(id)
        .username(username)
        .age(age)
        .build();
  }

  public static UserDto toDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .age(user.getAge())
        .build();
  }
}
