package com.mininetflix.ministreaming.application.profilefavorite.port;

import java.util.List;

import java.util.UUID;

import com.mininetflix.ministreaming.domain.profilefavorite.ProfileFavorite;

public interface ProfileFavoriteRepository {

        void save(ProfileFavorite favorite);

        boolean existsByProfileAndVideo(
                        UUID profileId,
                        String videoId);

        List<ProfileFavorite> findByProfile(
                        UUID profileId);

        void deleteByProfileAndVideo(
                        UUID profileId,
                        String videoId);
}