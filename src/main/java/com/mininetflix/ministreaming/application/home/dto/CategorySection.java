package com.mininetflix.ministreaming.application.home.dto;

import java.util.List;
import java.util.Map;

import com.mininetflix.ministreaming.domain.content.VideoCategory;

public record CategorySection(
                VideoCategory category,
                List<VideoSummaryResponse> videos) {
}