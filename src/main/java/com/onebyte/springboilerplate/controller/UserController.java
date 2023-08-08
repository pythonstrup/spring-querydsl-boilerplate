package com.onebyte.springboilerplate.controller;

import com.onebyte.springboilerplate.dto.ApiResponse;
import com.onebyte.springboilerplate.dto.UserDto;
import com.onebyte.springboilerplate.dto.UserSearchCondition;
import com.onebyte.springboilerplate.service.UserService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/v1/users/{id}")
  public ResponseEntity<ApiResponse<UserDto>> findUser(@PathVariable int id) {
    UserDto result = userService.findUser(id);
    ApiResponse<UserDto> response = new ApiResponse<>(result, "");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/v1/users/search")
  public ResponseEntity<ApiResponse<List<UserDto>>> findUser(@ModelAttribute UserSearchCondition search) {
    List<UserDto> result = userService.searchUser(search);
    ApiResponse<List<UserDto>> response = new ApiResponse<>(result, "");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/v1/users")
  public ResponseEntity<ApiResponse<List<UserDto>>> findUserAll() {
    List<UserDto> result = userService.findUserAll();
    ApiResponse<List<UserDto>> response = new ApiResponse<>(result, "");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/v1/users")
  public ResponseEntity<ApiResponse<UserDto>> saveUser(@RequestBody UserDto user) {
    UserDto result = userService.save(user);
    ApiResponse<UserDto> response = new ApiResponse<>(result, "");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("/v1/users/{id}")
  public ResponseEntity<ApiResponse<UserDto>> updateUser(
      @PathVariable int id,
      @RequestBody UserDto userDto) {
    UserDto result = userService.update(id, userDto);
    ApiResponse<UserDto> response = new ApiResponse<>(result, "");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
