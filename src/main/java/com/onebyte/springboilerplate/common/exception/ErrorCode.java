package com.onebyte.springboilerplate.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  BAD_REQUEST(HttpStatus.BAD_REQUEST, ExceptionName.BAD_REQUEST, ExceptionMessage.BAD_REQUEST.getMessage()),
  ;

  private final HttpStatus status;
  private final ExceptionName name;
  private final String message;
}
