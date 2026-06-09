package com.mininetflix.ministreaming.application.content.usecase;

import com.mininetflix.ministreaming.application.content.mappers.VideoResponseMapper;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListVideosUseCaseImpl
        implements ListVideosUseCase {

    private final VideoResponseMapper videoResponseMapper;
    private final VideoCatalogRepository repository;

    @Override
    public List<VideoResponse> execute() {

        return repository.findAll()
                .stream()
                .map(videoResponseMapper::toResponse)
                .toList();
    }
}