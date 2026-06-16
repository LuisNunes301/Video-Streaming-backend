package com.mininetflix.ministreaming.application.home.dto;

import com.mininetflix.ministreaming.domain.content.VideoCategory;

public record TrendingVideoResponse(

        String videoId,

        String title,

        String thumbnailUrl,

        Double duration,

        VideoCategory category,

        Long views,

        Long completedViews) {
}