package com.mininetflix.ministreaming.application.content.port;

import com.mininetflix.ministreaming.domain.content.VideoCategory;
import com.mininetflix.ministreaming.domain.content.VideoContent;
import java.util.List;
import java.util.Optional;

public interface VideoCatalogRepository {
  void save(VideoContent video);

  Optional<VideoContent> findById(String id);

  List<VideoContent> findAll();

  List<VideoContent> findByCategory(VideoCategory category);

  List<VideoContent> searchByTitle(String query);

  List<VideoContent> findByIds(List<String> ids);
}
