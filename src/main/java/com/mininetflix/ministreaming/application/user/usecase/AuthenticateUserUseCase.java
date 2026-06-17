package com.mininetflix.ministreaming.application.user.usecase;

import com.mininetflix.ministreaming.application.user.dto.AuthenticateUserInput;
import com.mininetflix.ministreaming.application.user.dto.AuthenticateUserOutput;

public interface AuthenticateUserUseCase {
  AuthenticateUserOutput execute(AuthenticateUserInput input);
}
