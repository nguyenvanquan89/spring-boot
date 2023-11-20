package com.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateItemException extends Exception {

  private static final long serialVersionUID = 1L;

  public DuplicateItemException(String message) {
    super(message);
  }
}
