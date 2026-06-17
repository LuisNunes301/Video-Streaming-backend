package com.mininetflix.ministreaming.infrastructure.profilefavorite.repository;

import com.mininetflix.ministreaming.infrastructure.profilefavorite.entity.ProfileFavoriteEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ProfileFavoriteJpaRepository extends JpaRepository<ProfileFavoriteEntity, UUID> {

  boolean existsByProfileIdAndVideoId(UUID profileId, String videoId);

  List<ProfileFavoriteEntity> findByProfileId(UUID profileId);

  @Modifying
  @Transactional
  void deleteByProfileIdAndVideoId(UUID profileId, String videoId);
}
