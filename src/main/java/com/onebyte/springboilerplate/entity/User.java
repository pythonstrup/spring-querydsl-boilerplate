package com.onebyte.springboilerplate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class User {

  @Id @GeneratedValue
  private Integer id;
  private String username;

  public User(Integer id, String username) {
    this.id = id;
    this.username = username;
  }
}
