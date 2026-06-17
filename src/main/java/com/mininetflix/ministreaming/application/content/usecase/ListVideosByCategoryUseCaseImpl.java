package com.mininetflix.ministreaming.application.content.usecase;

import com.mininetflix.ministreaming.application.content.mappers.VideoResponseMapper;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;

import com.mininetflix.ministreaming.domain.content.VideoCategory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListVideosByCategoryUseCaseImpl
                implements ListVideosByCategoryUseCase {

        private final VideoResponseMapper videoResponseMapper;
        private final VideoCatalogRepository repository;

        @Override
        public List<VideoResponse> execute(VideoCategory category) {

                return repository.findByCategory(category)
                                .stream()
                                .map(videoResponseMapper::toResponse)
                                .toList();
        }
}