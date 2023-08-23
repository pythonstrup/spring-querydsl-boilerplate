package com.onebyte.springboilerplate.domain.dto.user;

import com.onebyte.springboilerplate.domain.entity.User;
import java.util.Objects;
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
  private String email;
  private Integer age;

  public User toEntity() {
    return User.builder()
        .id(id)
        .username(username)
        .email(email)
        .age(age)
        .build();
  }

  public static UserDto toDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .age(user.getAge())
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDto userDto = (UserDto) o;
    return Objects.equals(id, userDto.id) && Objects.equals(username,
        userDto.username) && Objects.equals(age, userDto.age);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, age);
  }
}
