package com.mininetflix.ministreaming.application.home.usecase;

import java.util.List;

import com.mininetflix.ministreaming.application.home.dto.TrendingVideoResponse;

public interface GetTrendingVideosUseCase {

    List<TrendingVideoResponse> execute();
}
