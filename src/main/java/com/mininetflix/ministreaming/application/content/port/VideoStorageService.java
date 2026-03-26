package com.mininetflix.ministreaming.application.content.port;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface VideoStorageService {

    String generatePresignedUrl(String objectKey);

    void upload(String objectKey, MultipartFile file);

    void uploadFile(String objectKey, File file);

    File download(String objectKey);
}