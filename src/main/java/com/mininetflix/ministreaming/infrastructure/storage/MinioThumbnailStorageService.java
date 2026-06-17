package com.mininetflix.ministreaming.infrastructure.storage;

import com.mininetflix.ministreaming.application.storage.StorageBucketEnum;
import com.mininetflix.ministreaming.application.storage.StorageService;
import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MinioThumbnailStorageService implements ThumbnailStorageService {

  private final StorageService storageService;

  @Override
  public void upload(String objectKey, File file) {

    storageService.uploadFile(StorageBucketEnum.THUMBNAILS, objectKey, file);
  }

  @Override
  public String getPublicUrl(String objectKey) {

    return storageService.generatePublicUrl(StorageBucketEnum.THUMBNAILS, objectKey);
  }
}
