package com.onebyte.springboilerplate.service;

import com.onebyte.springboilerplate.dto.UserDto;
import com.onebyte.springboilerplate.dto.UserSearchCondition;
import com.onebyte.springboilerplate.entity.User;
import com.onebyte.springboilerplate.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;

  public UserDto findUser(int id) {
    User user = userRepository.findUser(id);
    return UserDto.toDto(user);
  }

  public UserDto save(UserDto userDto) {
    User savedUser = userRepository.save(userDto.toEntity());
    return UserDto.toDto(savedUser);
  }

  public UserDto update(UserDto userDto) {
    User findUser = userRepository.findUser(userDto.getId());
    findUser.setUsername(userDto.getUsername());
    return UserDto.toDto(findUser);
  }

  public List<UserDto> searchUser(UserSearchCondition search) {
    List<User> users = userRepository.searchUser(search);
    return users.stream()
        .map(user -> UserDto.toDto(user))
        .toList();
  }
}
