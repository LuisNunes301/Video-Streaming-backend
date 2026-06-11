package com.mininetflix.ministreaming.infrastructure.storage;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.application.storage.StorageBucketEnum;
import com.mininetflix.ministreaming.application.storage.StorageService;
import com.mininetflix.ministreaming.application.userprofile.port.AvatarStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinioAvatarStorageService
                implements AvatarStorageService {

        private final StorageService storageService;

        @Override
        public String uploadAvatar(MultipartFile file) {

                String avatarKey = UUID.randomUUID() + "-"
                                + file.getOriginalFilename();

                storageService.upload(
                                StorageBucketEnum.AVATARS,
                                avatarKey,
                                file);

                return avatarKey;
        }

        @Override
        public void deleteAvatar(String avatarKey) {

                storageService.delete(
                                StorageBucketEnum.AVATARS,
                                avatarKey);
        }
}