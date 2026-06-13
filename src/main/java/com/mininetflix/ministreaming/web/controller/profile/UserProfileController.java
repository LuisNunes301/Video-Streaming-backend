package com.mininetflix.ministreaming.web.controller.profile;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.application.profilefavorite.dto.FavoriteResponse;
import com.mininetflix.ministreaming.application.profilefavorite.usecase.AddFavoriteUseCase;
import com.mininetflix.ministreaming.application.profilefavorite.usecase.ListFavoritesUseCase;
import com.mininetflix.ministreaming.application.profilefavorite.usecase.RemoveFavoriteUseCase;
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
        private final AddFavoriteUseCase addFavoriteUseCase;

        private final RemoveFavoriteUseCase removeFavoriteUseCase;

        private final ListFavoritesUseCase listFavoritesUseCase;

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

        @PostMapping("/favorites/{videoId}")
        public ResponseEntity<Void> addFavorite(
                        Authentication authentication,
                        @PathVariable String videoId) {

                UUID userId = UUID.fromString(authentication.getName());

                addFavoriteUseCase.execute(
                                userId,
                                videoId);

                return ResponseEntity.ok().build();
        }

        @DeleteMapping("/favorites/{videoId}")
        public ResponseEntity<Void> removeFavorite(
                        Authentication authentication,
                        @PathVariable String videoId) {

                System.out.println("DELETE FAVORITE HIT");

                UUID userId = UUID.fromString(authentication.getName());

                removeFavoriteUseCase.execute(
                                userId,
                                videoId);

                return ResponseEntity.noContent().build();
        }

        @GetMapping("/favorites")
        public ResponseEntity<List<FavoriteResponse>> listFavorites(
                        Authentication authentication) {

                UUID userId = UUID.fromString(authentication.getName());

                return ResponseEntity.ok(
                                listFavoritesUseCase.execute(userId));
        }
}