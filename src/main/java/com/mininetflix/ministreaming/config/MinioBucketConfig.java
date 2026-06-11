package com.mininetflix.ministreaming.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;

@Configuration
public class MinioBucketConfig {

        @Bean
        public Object createBuckets(
                        MinioClient minioClient,
                        @Value("${minio.video-bucket}") String videoBucket,
                        @Value("${minio.thumbnail-bucket}") String thumbnailBucket,
                        @Value("${minio.avatar-bucket}") String avatarBucket)
                        throws Exception {

                createBucketIfNotExists(
                                minioClient,
                                videoBucket);

                createBucketIfNotExists(
                                minioClient,
                                thumbnailBucket);

                createBucketIfNotExists(
                                minioClient,
                                avatarBucket);

                return new Object();
        }

        private void createBucketIfNotExists(
                        MinioClient minioClient,
                        String bucketName)
                        throws Exception {

                boolean exists = minioClient.bucketExists(
                                BucketExistsArgs.builder()
                                                .bucket(bucketName)
                                                .build());

                if (!exists) {

                        minioClient.makeBucket(
                                        MakeBucketArgs.builder()
                                                        .bucket(bucketName)
                                                        .build());

                        System.out.println(
                                        "Bucket criado: " + bucketName);
                }
        }
}