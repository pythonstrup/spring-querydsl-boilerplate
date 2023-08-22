package com.onebyte.springboilerplate.domain.entity;

import jakarta.persistence.Column;
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
  @Column(unique = true, length = 40)
  private String username;
  @Column(length = 255)
  private String password;
  private String email;
  private Integer age;

  public User(Integer id, String username, String password, String email, Integer age) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.age = age;
  }
}
