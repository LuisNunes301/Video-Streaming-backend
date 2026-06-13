package com.mininetflix.ministreaming.application.profilefavorite.usecase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.profilefavorite.dto.FavoriteResponse;
import com.mininetflix.ministreaming.application.profilefavorite.port.ProfileFavoriteRepository;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListFavoritesUseCaseImpl
                implements ListFavoritesUseCase {

        private final ProfileFavoriteRepository repository;
        private final UserProfileRepository profileRepository;
        private final VideoCatalogRepository videoRepository;

        @Override
        public List<FavoriteResponse> execute(
                        UUID userId) {

                UUID profileId = profileRepository
                                .findByUserId(userId)
                                .orElseThrow(() -> new IllegalArgumentException("Profile not found"))
                                .getId();

                return repository
                                .findByProfile(profileId)
                                .stream()
                                .map(favorite -> videoRepository
                                                .findById(favorite.getVideoId())
                                                .map(video -> new FavoriteResponse(
                                                                video.getId(),
                                                                video.getTitle(),
                                                                video.getThumbnailKey(),
                                                                video.getCategory())))
                                .flatMap(Optional::stream)
                                .toList();
        }
}