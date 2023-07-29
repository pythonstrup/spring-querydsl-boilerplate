package com.onebyte.springboilerplate.dto;

import com.onebyte.springboilerplate.entity.UserEntity;
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

  public UserEntity toEntity() {
    return UserEntity.builder()
        .id(id)
        .username(username)
        .build();
  }
}
