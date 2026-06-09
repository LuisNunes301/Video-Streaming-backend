package com.mininetflix.ministreaming.application.content.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;

@Service
public class ListVideosUseCaseImpl implements ListVideosUseCase {

    private final VideoCatalogRepository repository;
    private final VideoStorageService storageService;

    public ListVideosUseCaseImpl(VideoCatalogRepository repository, VideoStorageService storageService) {
        this.repository = repository;
        this.storageService = storageService;
    }

    @Override
    public List<VideoResponse> execute() {

        return repository.findAll()
                .stream()
                .map(video -> new VideoResponse(
                        video.getId(),
                        video.getTitle(),
                        video.getStatus(),
                        video.getCategory(),
                        video.getDuration(),
                        storageService.generatePublicUrl(
                                video.getThumbnailKey())))
                .toList();
    }

}
