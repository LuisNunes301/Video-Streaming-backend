package com.mininetflix.ministreaming.application.userprofile.usecase;

import com.mininetflix.ministreaming.web.controller.profile.dto.UserProfileResponse;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UploadAvatarUseCase {

  UserProfileResponse execute(UUID userId, MultipartFile file);
}
