package com.onebyte.springboilerplate.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{

  private final HttpStatus status;
  private final ExceptionName name;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    status = errorCode.getStatus();
    name = errorCode.getName();
  }
}
