package com.mininetflix.ministreaming.application.user.usecase;

import com.mininetflix.ministreaming.application.user.dto.GetCurrentUserOutput;

public interface GetCurrentUserUseCase {
    GetCurrentUserOutput execute(String email);
}