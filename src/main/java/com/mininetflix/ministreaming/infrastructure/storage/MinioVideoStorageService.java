package com.mininetflix.ministreaming.infrastructure.storage;

import java.io.File;

import org.springframework.stereotype.Component;

import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.storage.StorageBucketEnum;
import com.mininetflix.ministreaming.application.storage.StorageService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MinioVideoStorageService
                implements VideoStorageService {

        private final StorageService storageService;

        @Override
        public void upload(
                        String objectKey,
                        MultipartFile file) {

                storageService.upload(
                                StorageBucketEnum.VIDEOS,
                                objectKey,
                                file);
        }

        @Override
        public File download(
                        String objectKey) {

                return storageService.download(
                                StorageBucketEnum.VIDEOS,
                                objectKey);
        }

        @Override
        public void uploadFile(
                        String objectKey,
                        File file) {

                storageService.uploadFile(
                                StorageBucketEnum.VIDEOS,
                                objectKey,
                                file);
        }

        @Override
        public String generatePublicUrl(
                        String objectKey) {

                return storageService.generatePublicUrl(
                                StorageBucketEnum.VIDEOS,
                                objectKey);
        }
}