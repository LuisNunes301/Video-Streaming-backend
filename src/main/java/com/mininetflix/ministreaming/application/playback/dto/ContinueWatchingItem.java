package com.mininetflix.ministreaming.application.playback.dto;

import com.mininetflix.ministreaming.domain.content.VideoCategory;

public record ContinueWatchingItem(
                String videoId,
                String title,
                String thumbnailUrl,
                Double duration,
                Double currentTime,
                Integer progressPercent,
                VideoCategory category) {
}