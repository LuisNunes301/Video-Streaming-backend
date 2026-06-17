package com.mininetflix.ministreaming.infrastructure.user.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findById(UUID id);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByName(String name);

  boolean existsByEmail(String email);

  boolean existsByName(String name);
}
