package com.mininetflix.ministreaming.application.profilefavorite.port;

import com.mininetflix.ministreaming.domain.profilefavorite.ProfileFavorite;
import java.util.List;
import java.util.UUID;

public interface ProfileFavoriteRepository {

  void save(ProfileFavorite favorite);

  boolean existsByProfileAndVideo(UUID profileId, String videoId);

  List<ProfileFavorite> findByProfile(UUID profileId);

  void deleteByProfileAndVideo(UUID profileId, String videoId);
}
