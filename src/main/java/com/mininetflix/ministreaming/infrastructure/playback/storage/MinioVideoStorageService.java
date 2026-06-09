package com.mininetflix.ministreaming.infrastructure.playback.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.application.content.port.VideoStorageService;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Component
public class MinioVideoStorageService implements VideoStorageService {

    private final MinioClient minioClient;
    private final String publicHost;
    private final String bucket;

    public MinioVideoStorageService(
            MinioClient minioClient,
            @Value("${minio.public-url}") String publicHost,
            @Value("${minio.bucket}") String bucket) {

        this.minioClient = minioClient;
        this.publicHost = publicHost;
        this.bucket = bucket;
    }

    @Override
    public String generatePublicUrl(String objectKey) {

        return publicHost
                + "/videos/"
                + objectKey;
    }

    @Override
    public void upload(String objectKey, MultipartFile file) {

        try {

            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucket)
                            .build());

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucket)
                                .build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(
                                    file.getInputStream(),
                                    file.getSize(),
                                    -1)
                            .contentType(file.getContentType())
                            .build());

        } catch (Exception e) {
            throw new RuntimeException("Upload failed", e);
        }
    }

    @Override
    public File download(String objectKey) {

        try {

            File tempFile = File.createTempFile("video-", ".tmp");

            minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build())
                    .transferTo(
                            new FileOutputStream(tempFile));

            return tempFile;

        } catch (Exception e) {
            throw new RuntimeException("Download failed", e);
        }
    }

    @Override
    public void uploadFile(String objectKey, File file) {

        try {

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(
                                    new FileInputStream(file),
                                    file.length(),
                                    -1)
                            .build());

        } catch (Exception e) {
            throw new RuntimeException("Upload file failed", e);
        }
    }
}