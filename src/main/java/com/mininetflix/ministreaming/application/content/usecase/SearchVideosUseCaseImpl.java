package com.mininetflix.ministreaming.application.content.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import com.mininetflix.ministreaming.application.content.mappers.VideoResponseMapper;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchVideosUseCaseImpl
                implements SearchVideosUseCase {

        private final VideoCatalogRepository repository;
        private final VideoResponseMapper videoResponseMapper;

        @Override
        public List<VideoResponse> execute(String query) {

                return repository.searchByTitle(query)
                                .stream()
                                .map(videoResponseMapper::toResponse)
                                .toList();
        }
}