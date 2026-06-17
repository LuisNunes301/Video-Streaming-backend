package com.mininetflix.ministreaming.infrastructure.user.security;

import com.mininetflix.ministreaming.application.user.port.TokenService;
import com.mininetflix.ministreaming.domain.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class JwtTokenService implements TokenService {

  private final JwtProperties properties;

  public JwtTokenService(JwtProperties properties) {
    this.properties = properties;
  }

  @Override
  public String generateToken(String userId, Set<UserRole> roles) {

    Date now = new Date();
    Date expiration = new Date(now.getTime() + properties.getExpiration());

    return Jwts.builder()
        .setSubject(userId)
        .claim("roles", roles.stream().map(Enum::name).toList())
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(
            Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8)),
            SignatureAlgorithm.HS512)
        .compact();
  }

  @Override
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(
              Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8)))
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public String getSubject(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8)))
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  @Override
  public List<String> getRoles(String token) {

    Claims claims =
        Jwts.parserBuilder()
            .setSigningKey(
                Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .getBody();

    Object rolesClaim = claims.get("roles");

    if (rolesClaim instanceof List<?> rolesList) {
      return rolesList.stream().map(Object::toString).toList();
    }

    return List.of();
  }
}
