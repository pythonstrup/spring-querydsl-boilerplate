package com.onebyte.springboilerplate.controller;

import com.onebyte.springboilerplate.dto.UserDto;
import com.onebyte.springboilerplate.dto.UserResponse;
import com.onebyte.springboilerplate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/v1/user")
  public ResponseEntity<UserResponse> saveUser(@RequestBody UserDto user) {
    int savedId = userService.save(user);
    UserResponse response = UserResponse.builder().id(savedId).build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
