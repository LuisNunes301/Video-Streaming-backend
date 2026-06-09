package com.mininetflix.ministreaming.application.content.mappers;

import org.springframework.stereotype.Component;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.domain.content.VideoContent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VideoResponseMapper {

    private final VideoStorageService storageService;

    public VideoResponse toResponse(VideoContent video) {

        String thumbnailUrl = video.getThumbnailKey() == null
                ? null
                : storageService.generatePublicUrl(
                        video.getThumbnailKey());

        return new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getStatus(),
                video.getCategory(),
                video.getDuration(),
                thumbnailUrl);
    }
}
