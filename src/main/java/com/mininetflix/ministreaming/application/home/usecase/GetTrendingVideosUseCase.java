package com.mininetflix.ministreaming.application.home.usecase;

import com.mininetflix.ministreaming.application.home.dto.TrendingVideoResponse;
import java.util.List;

public interface GetTrendingVideosUseCase {

  List<TrendingVideoResponse> execute();
}
