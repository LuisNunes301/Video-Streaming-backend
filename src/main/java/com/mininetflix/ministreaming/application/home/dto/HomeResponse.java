package com.mininetflix.ministreaming.application.home.dto;

import java.util.List;

public record HomeResponse(
    List<VideoSummaryResponse> trending,
    List<VideoSummaryResponse> continueWatching,
    List<CategorySection> categories) {}
