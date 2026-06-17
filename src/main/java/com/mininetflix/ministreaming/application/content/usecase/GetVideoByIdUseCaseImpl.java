package com.mininetflix.ministreaming.application.content.usecase;

import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import com.mininetflix.ministreaming.application.content.mappers.VideoResponseMapper;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetVideoByIdUseCaseImpl implements GetVideoByIdUseCase {

  private final VideoCatalogRepository repository;
  private final VideoResponseMapper mapper;

  @Override
  @Cacheable("video-by-id")
  public VideoResponse execute(String videoId) {

    return repository.findById(videoId).map(mapper::toResponse).orElseThrow();
  }
}
