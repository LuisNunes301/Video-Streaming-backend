package com.mininetflix.ministreaming.application.content.port;

import java.util.List;
import java.util.Optional;

import com.mininetflix.ministreaming.domain.content.VideoCategory;
import com.mininetflix.ministreaming.domain.content.VideoContent;

public interface VideoCatalogRepository {
    void save(VideoContent video);

    Optional<VideoContent> findById(String id);

    List<VideoContent> findAll();

    List<VideoContent> findByCategory(VideoCategory category);

}
