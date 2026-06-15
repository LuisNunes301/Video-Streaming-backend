package com.mininetflix.ministreaming.application.home.usecase;

import com.mininetflix.ministreaming.application.home.dto.HomeResponse;

public interface GetHomeUseCase {

    HomeResponse execute(String userId);
}