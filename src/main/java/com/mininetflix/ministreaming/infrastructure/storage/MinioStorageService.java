package com.mininetflix.ministreaming.infrastructure.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.application.storage.StorageBucketEnum;
import com.mininetflix.ministreaming.application.storage.StorageService;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinioStorageService
                implements StorageService {

        private final MinioClient minioClient;
        private final StorageBucketResolver bucketResolver;

        @Value("${minio.public-url}")
        private String publicUrl;

        @Override
        public void upload(
                        StorageBucketEnum bucket,
                        String objectKey,
                        MultipartFile file) {

                try {

                        minioClient.putObject(
                                        PutObjectArgs.builder()
                                                        .bucket(bucketResolver.resolve(bucket))
                                                        .object(objectKey)
                                                        .stream(
                                                                        file.getInputStream(),
                                                                        file.getSize(),
                                                                        -1)
                                                        .contentType(file.getContentType())
                                                        .build());

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Failed to upload object",
                                        e);
                }
        }

        @Override
        public void uploadFile(
                        StorageBucketEnum bucket,
                        String objectKey,
                        File file) {

                try {

                        minioClient.putObject(
                                        PutObjectArgs.builder()
                                                        .bucket(bucketResolver.resolve(bucket))
                                                        .object(objectKey)
                                                        .stream(
                                                                        new FileInputStream(file),
                                                                        file.length(),
                                                                        -1)
                                                        .build());

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Failed to upload file",
                                        e);
                }
        }

        @Override
        public File download(
                        StorageBucketEnum bucket,
                        String objectKey) {

                try {

                        File tempFile = File.createTempFile(
                                        "storage-",
                                        ".tmp");

                        minioClient.getObject(
                                        GetObjectArgs.builder()
                                                        .bucket(bucketResolver.resolve(bucket))
                                                        .object(objectKey)
                                                        .build())
                                        .transferTo(
                                                        new FileOutputStream(tempFile));

                        return tempFile;

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Failed to download object",
                                        e);
                }
        }

        @Override
        public void delete(
                        StorageBucketEnum bucket,
                        String objectKey) {

                try {

                        minioClient.removeObject(
                                        RemoveObjectArgs.builder()
                                                        .bucket(bucketResolver.resolve(bucket))
                                                        .object(objectKey)
                                                        .build());

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Failed to delete object",
                                        e);
                }
        }

        @Override
        public String generatePublicUrl(
                        StorageBucketEnum bucket,
                        String objectKey) {

                return switch (bucket) {

                        case VIDEOS ->
                                publicUrl + "/videos/" + objectKey;

                        case THUMBNAILS ->
                                publicUrl + "/thumbnails/" + objectKey;

                        case AVATARS ->
                                publicUrl + "/avatars/" + objectKey;
                };
        }
}