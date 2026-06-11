package com.mininetflix.ministreaming.web.controller.profile;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileRequest;
import com.mininetflix.ministreaming.application.userprofile.usecase.GetUserProfileUseCase;
import com.mininetflix.ministreaming.application.userprofile.usecase.UpdateProfileUseCase;
import com.mininetflix.ministreaming.application.userprofile.usecase.UploadAvatarUseCase;
import com.mininetflix.ministreaming.web.controller.profile.dto.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class UserProfileController {

        private final GetUserProfileUseCase getUserProfileUseCase;
        private final UpdateProfileUseCase updateProfileUseCase;
        private final UploadAvatarUseCase uploadAvatarUseCase;

        @GetMapping("/me")
        public ResponseEntity<UserProfileResponse> me(
                        Authentication authentication) {

                UserProfileResponse response = getUserProfileUseCase.execute(authentication.getName());

                return ResponseEntity.ok(response);
        }

        @PutMapping
        public ResponseEntity<UserProfileResponse> update(
                        Authentication authentication,
                        @RequestBody UpdateProfileRequest request) {

                UUID userId = UUID.fromString(authentication.getName());

                UserProfileResponse response = updateProfileUseCase.execute(userId, request);

                return ResponseEntity.ok(response);
        }

        @PostMapping("/avatar")
        public ResponseEntity<UserProfileResponse> uploadAvatar(
                        Authentication authentication,
                        @RequestParam("file") MultipartFile file) {

                UUID userId = UUID.fromString(authentication.getName());

                UserProfileResponse response = uploadAvatarUseCase.execute(
                                userId,
                                file);

                return ResponseEntity.ok(response);
        }
}