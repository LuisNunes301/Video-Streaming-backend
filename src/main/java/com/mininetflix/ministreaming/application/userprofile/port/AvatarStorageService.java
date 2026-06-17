package com.mininetflix.ministreaming.application.userprofile.port;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarStorageService {

  String uploadAvatar(MultipartFile file);

  void deleteAvatar(String avatarKey);
}
