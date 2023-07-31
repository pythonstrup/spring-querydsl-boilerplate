package com.onebyte.springboilerplate.controller;

import com.onebyte.springboilerplate.dto.UserDto;
import com.onebyte.springboilerplate.dto.UserSearchCondition;
import com.onebyte.springboilerplate.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/v1/user/{id}")
  public ResponseEntity<UserDto> findUser(@PathVariable int id) {
    UserDto response = userService.findUser(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/v1/user/search")
  public ResponseEntity<List<UserDto>> findUser(@RequestBody UserSearchCondition search) {
    List<UserDto> response = userService.searchUser(search);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/v1/user")
  public ResponseEntity<UserDto> saveUser(@RequestBody UserDto user) {
    UserDto response = userService.save(user);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("/v1/user")
  public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
    UserDto response = userService.update(userDto);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
