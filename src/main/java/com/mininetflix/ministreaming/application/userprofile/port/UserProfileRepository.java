package com.mininetflix.ministreaming.application.userprofile.port;

import com.mininetflix.ministreaming.domain.userprofile.UserProfile;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository {

  UserProfile save(UserProfile profile);

  Optional<UserProfile> findById(UUID profileId);

  Optional<UserProfile> findByUserId(UUID userId);

  void delete(UUID profileId);
}
