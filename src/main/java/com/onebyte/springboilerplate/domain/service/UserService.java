package com.onebyte.springboilerplate.domain.service;

import com.onebyte.springboilerplate.domain.dto.user.UserDto;
import com.onebyte.springboilerplate.domain.dto.user.UserSearchCondition;
import com.onebyte.springboilerplate.domain.dto.user.UserSignInRequest;
import com.onebyte.springboilerplate.domain.entity.User;
import com.onebyte.springboilerplate.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDto findUser(int id) {
    User user = userRepository.findUser(id);
    return UserDto.toDto(user);
  }

  public Optional<User> findUserByUsername(String username) {
    Optional<User> result = userRepository.findUserByUsername(username);
    return result;
  }

  public UserDto save(UserSignInRequest request) {
    request.encodePassword(passwordEncoder.encode(request.getPassword()));
    User savedUser = userRepository.save(request.toEntity());
    return UserDto.toDto(savedUser);
  }

  public UserDto update(int id, UserDto userDto) {
    User findUser = userRepository.findUser(id);
    if (userDto.getUsername() != null) {
      findUser.setUsername(userDto.getUsername());
    }
    if (userDto.getAge() != null) {
      findUser.setAge(userDto.getAge());
    }
    return UserDto.toDto(findUser);
  }

  public List<UserDto> searchUser(UserSearchCondition search) {
    List<User> users = userRepository.searchUser(search);
    return users.stream()
        .map(user -> UserDto.toDto(user))
        .toList();
  }

  public List<UserDto> findUserAll() {
    List<User> userList = userRepository.findUserAll();
    return userList.stream()
        .map(user -> UserDto.toDto(user))
        .collect(Collectors.toList());
  }
}
