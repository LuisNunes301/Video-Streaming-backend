package com.mininetflix.ministreaming.application.content.dto;

import com.mininetflix.ministreaming.domain.content.VideoCategory;
import com.mininetflix.ministreaming.domain.content.VideoStatus;

public record VideoResponse(
    String id,
    String title,
    VideoStatus status,
    VideoCategory category,
    Double duration,
    String thumbnailUrl) {}
