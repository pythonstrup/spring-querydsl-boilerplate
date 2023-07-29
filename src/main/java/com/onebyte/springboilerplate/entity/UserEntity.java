package com.onebyte.springboilerplate.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class UserEntity {

  @Id @GeneratedValue
  private Integer id;
  private String username;

  public UserEntity(Integer id, String username) {
    this.id = id;
    this.username = username;
  }
}
