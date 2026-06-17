package com.mininetflix.ministreaming.application.user.port;

import com.mininetflix.ministreaming.domain.user.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  User save(User user);

  Optional<User> findByEmail(String email);

  Optional<User> findByName(String name);

  boolean existsByEmail(String email);

  boolean existsByName(String name);

  Optional<User> findById(UUID id);
}
