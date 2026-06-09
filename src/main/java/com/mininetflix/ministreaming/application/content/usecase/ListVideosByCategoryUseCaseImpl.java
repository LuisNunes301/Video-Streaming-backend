package com.mininetflix.ministreaming.application.content.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.domain.content.VideoCategory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListVideosByCategoryUseCaseImpl
        implements ListVideosByCategoryUseCase {

    private final VideoCatalogRepository repository;
    private final VideoStorageService storageService;

    @Override
    public List<VideoResponse> execute(VideoCategory category) {

        return repository.findByCategory(category)
                .stream()
                .map(video -> {

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
                })
                .toList();
    }
}
