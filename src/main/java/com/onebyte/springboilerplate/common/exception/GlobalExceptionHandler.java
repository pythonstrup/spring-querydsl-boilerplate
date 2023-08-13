package com.onebyte.springboilerplate.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<CustomErrorResponse> handleRuntimeError(final RuntimeException e) {
    log.error("Uncontrolled Exception ", e);
    return makeResponseEntity(ExceptionName.RUNTIME_ERROR.getName(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomErrorResponse> handleInternalServerError(final RuntimeException e) {
    log.error("Uncontrolled Exception ", e);
    return makeResponseEntity(ExceptionName.INTERNAL_SERVER_ERROR.getName(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<CustomErrorResponse> makeResponseEntity(
      String name,
      String message,
      HttpStatus httpStatus) {
    CustomErrorResponse response = CustomErrorResponse.builder()
        .name(name)
        .message(message)
        .build();
    return new ResponseEntity<>(response, httpStatus);
  }
}
