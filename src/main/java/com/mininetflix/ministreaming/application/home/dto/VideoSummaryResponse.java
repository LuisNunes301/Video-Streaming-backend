package com.mininetflix.ministreaming.application.home.dto;

import com.mininetflix.ministreaming.domain.content.VideoCategory;

public record VideoSummaryResponse(
        String id,
        String title,
        String thumbnailUrl,
        Double duration,
        VideoCategory category) {
}
