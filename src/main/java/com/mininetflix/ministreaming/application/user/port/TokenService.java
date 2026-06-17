package com.mininetflix.ministreaming.application.user.port;

import com.mininetflix.ministreaming.domain.user.UserRole;
import java.util.List;
import java.util.Set;

public interface TokenService {

  String generateToken(String userId, Set<UserRole> roles);

  boolean validateToken(String token);

  String getSubject(String token);

  List<String> getRoles(String token);
}
