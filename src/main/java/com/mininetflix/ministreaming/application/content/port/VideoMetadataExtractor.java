package com.mininetflix.ministreaming.application.content.port;

import com.mininetflix.ministreaming.application.content.dto.VideoMetadata;

public interface VideoMetadataExtractor {
  VideoMetadata extract(String objectKey);
}
