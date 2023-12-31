package com.onebyte.springboilerplate.domain.controller;

import com.onebyte.springboilerplate.domain.dto.user.UserSignInRequest;
import com.onebyte.springboilerplate.domain.service.UserService;
import com.onebyte.springboilerplate.common.dto.ApiResponse;
import com.onebyte.springboilerplate.domain.dto.user.UserDto;
import com.onebyte.springboilerplate.domain.dto.user.UserSearchCondition;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<UserDto>> findUser(@PathVariable int id) {
    UserDto result = userService.findUser(id);
    ApiResponse<UserDto> response = new ApiResponse<>(result, "");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<ApiResponse<List<UserDto>>> findUser(@ModelAttribute UserSearchCondition search) {
    List<UserDto> result = userService.searchUser(search);
    ApiResponse<List<UserDto>> response = new ApiResponse<>(result, "");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<UserDto>>> findUserAll() {
    List<UserDto> result = userService.findUserAll();
    ApiResponse<List<UserDto>> response = new ApiResponse<>(result, "");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/signin")
  public ResponseEntity<ApiResponse<UserDto>> saveUser(@RequestBody UserSignInRequest user) {
    UserDto result = userService.save(user);
    ApiResponse<UserDto> response = new ApiResponse<>(result, "");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<UserDto>> updateUser(
      @PathVariable int id,
      @RequestBody UserDto userDto) {
    UserDto result = userService.update(id, userDto);
    ApiResponse<UserDto> response = new ApiResponse<>(result, "");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/error")
  public void error() {
    throw new RuntimeException("user runtime exception");
  }
}
