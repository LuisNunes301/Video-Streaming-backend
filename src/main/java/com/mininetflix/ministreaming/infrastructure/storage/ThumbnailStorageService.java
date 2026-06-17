package com.mininetflix.ministreaming.infrastructure.storage;

import java.io.File;

public interface ThumbnailStorageService {

  void upload(String objectKey, File file);

  String getPublicUrl(String objectKey);
}
