package com.mininetflix.ministreaming.application.home.dto;

import com.mininetflix.ministreaming.domain.content.VideoCategory;
import java.util.List;

public record CategorySection(VideoCategory category, List<VideoSummaryResponse> videos) {}
