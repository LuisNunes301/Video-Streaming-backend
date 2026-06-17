package com.mininetflix.ministreaming.domain.user;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {

  private final UUID id;
  private String name;
  private String email;
  private String passwordHash;
  private final LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private final Set<UserRole> roles;

  public User(String name, String email, String passwordHash) {
    this.id = null;
    this.name = name;
    this.email = email;
    this.passwordHash = passwordHash;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.roles = new HashSet<>();
    this.roles.add(UserRole.USER);
  }

  public User(
      UUID id,
      String name,
      String email,
      String passwordHash,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      Set<UserRole> roles) {

    this.id = id;
    this.name = name;
    this.email = email;
    this.passwordHash = passwordHash;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.roles = new HashSet<>(roles);
  }

  public void changeName(String newName) {
    this.name = newName;
    touch();
  }

  public void changeEmail(String newEmail) {
    this.email = newEmail;
    touch();
  }

  public void changePassword(String newPasswordHash) {
    this.passwordHash = newPasswordHash;
    touch();
  }

  public void addRole(UserRole role) {
    this.roles.add(role);
    touch();
  }

  public void removeRole(UserRole role) {
    this.roles.remove(role);
    touch();
  }

  public boolean hasRole(UserRole role) {
    return roles.contains(role);
  }

  private void touch() {
    this.updatedAt = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public Set<UserRole> getRoles() {
    return Set.copyOf(roles);
  }
}
