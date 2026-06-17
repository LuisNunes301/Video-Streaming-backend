package com.mininetflix.ministreaming.domain.user.exception;

public class NameAlreadyExistsExecption extends RuntimeException {

  public NameAlreadyExistsExecption(String name) {
    super("Name already registered: " + name);
  }
}
