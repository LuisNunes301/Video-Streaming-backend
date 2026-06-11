package com.mininetflix.ministreaming.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mininetflix.ministreaming.application.storage.StorageBucketEnum;

@Component
public class StorageBucketResolver {

    @Value("${minio.video-bucket}")
    private String videoBucket;

    @Value("${minio.thumbnail-bucket}")
    private String thumbnailBucket;

    @Value("${minio.avatar-bucket}")
    private String avatarBucket;

    public String resolve(StorageBucketEnum bucket) {

        return switch (bucket) {

            case VIDEOS -> videoBucket;
            case THUMBNAILS -> thumbnailBucket;
            case AVATARS -> avatarBucket;
        };
    }
}
