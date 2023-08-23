package com.onebyte.springboilerplate.domain.exception;

import com.onebyte.springboilerplate.common.exception.CustomException;
import com.onebyte.springboilerplate.common.exception.ErrorCode;

public class NotFoundException extends CustomException {

  public NotFoundException() {
    super(ErrorCode.NOT_FOUND);
  }
}
