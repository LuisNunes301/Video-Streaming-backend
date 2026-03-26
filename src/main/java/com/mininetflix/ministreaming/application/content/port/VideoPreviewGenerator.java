package com.mininetflix.ministreaming.application.content.port;

import com.mininetflix.ministreaming.application.content.dto.PreviewResult;

public interface VideoPreviewGenerator {
    PreviewResult generate(String videoId, String objectKey);
}
