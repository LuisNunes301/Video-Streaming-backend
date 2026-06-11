package com.mininetflix.ministreaming.infrastructure.storage;

import java.io.File;

import com.mininetflix.ministreaming.infrastructure.storage.ThumbnailStorageService;

public interface ThumbnailStorageService {

        void upload(
                        String objectKey,
                        File file);

        String getPublicUrl(
                        String objectKey);
}