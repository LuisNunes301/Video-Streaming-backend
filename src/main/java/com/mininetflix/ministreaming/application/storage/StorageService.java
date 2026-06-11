package com.mininetflix.ministreaming.application.storage;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

        void upload(
                        StorageBucketEnum bucket,
                        String objectKey,
                        MultipartFile file);

        void uploadFile(
                        StorageBucketEnum bucket,
                        String objectKey,
                        File file);

        File download(
                        StorageBucketEnum bucket,
                        String objectKey);

        void delete(
                        StorageBucketEnum bucket,
                        String objectKey);

        String generatePublicUrl(
                        StorageBucketEnum bucket,
                        String objectKey);
}