package com.onebyte.springboilerplate.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionName {
  BAD_REQUEST("Bad Request"),
  RUNTIME_ERROR("Runtime Error"),
  INTERNAL_SERVER_ERROR("Internal Server Error"),
  FORBIDDEN("Forbidden")
  ;

  private final String name;
}
