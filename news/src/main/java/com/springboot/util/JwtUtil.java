package com.springboot.util;

import com.springboot.dto.RoleDTO;
import com.springboot.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${security.jwt.secret.key}")
  private String secretKey;


  @Value("${security.jwt.token.validity}")
  private long tokenValidity;

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String userName = getUsernameFromToken(token);
    return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  /**
   * Generate token by userName, secret_key, token_expriced
   *
   * @param userDto user dto
   * @return
   */
  public String generateToken(UserDTO userDto) {
    Map<String, Object> claims = new HashMap<>(2, 1);
    List<String> roleCodes = userDto.getRoles().stream().map(RoleDTO::getCode)
        .collect(Collectors.toList());
    claims.put("roleCodes", roleCodes);
    claims.put("userId", userDto.getId());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDto.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + tokenValidity * 1000))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }
}
