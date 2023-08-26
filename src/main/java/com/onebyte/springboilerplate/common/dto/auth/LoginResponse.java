package com.onebyte.springboilerplate.common.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

  private String username;
}
