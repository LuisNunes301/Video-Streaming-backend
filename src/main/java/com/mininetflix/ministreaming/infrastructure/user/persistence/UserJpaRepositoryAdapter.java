package com.mininetflix.ministreaming.infrastructure.user.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.mininetflix.ministreaming.application.user.port.UserRepository;
import com.mininetflix.ministreaming.domain.user.User;

@Repository
public class UserJpaRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpa;

    public UserJpaRepositoryAdapter(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public User save(User user) {
        return jpa.save(UserEntity.fromDomain(user)).toDomain();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail(email).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByName(String name) {
        return jpa.findByName(name).map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpa.existsByEmail(email);
    }

    @Override
    public boolean existsByName(String name) {
        return jpa.existsByName(name);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpa.findById(id).map(UserEntity::toDomain);
    }
}
