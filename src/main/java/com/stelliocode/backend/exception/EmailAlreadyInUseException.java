package com.stelliocode.backend.exception;

public class EmailAlreadyInUseException extends RuntimeException {
  public EmailAlreadyInUseException(String message) {
    super(message);
  }
}