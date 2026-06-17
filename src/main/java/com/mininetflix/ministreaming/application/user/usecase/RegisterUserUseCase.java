package com.mininetflix.ministreaming.application.user.usecase;

import com.mininetflix.ministreaming.application.user.dto.RegisterUserInput;

public interface RegisterUserUseCase {
  void execute(RegisterUserInput input);
}
