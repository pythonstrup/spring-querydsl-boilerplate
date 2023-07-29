package com.onebyte.springboilerplate.service;

import com.onebyte.springboilerplate.dto.UserDto;
import com.onebyte.springboilerplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public int save(UserDto userDto) {
    return userRepository.save(userDto.toEntity());
  }
}
