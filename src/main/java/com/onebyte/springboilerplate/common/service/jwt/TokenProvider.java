package com.onebyte.springboilerplate.common.service.jwt;

import com.onebyte.springboilerplate.common.dto.auth.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

  private static final String AUTHORITY_KEY = "auth";
  private final String secret;
  private final long expires;
  private Key key;

  public TokenProvider(
      @Value("${auth.jwt.secret}") String secret,
      @Value("${auth.jwt.expires}") long expires
  ) {
    this.secret = secret;
    this.expires = expires;
  }

  @Override
  public void afterPropertiesSet() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(Authentication authentication) {

    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    long now = System.currentTimeMillis(); // Date getTime 메소드보다 우수

    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim(AUTHORITY_KEY, authorities)
        .signWith(key, SignatureAlgorithm.HS512)
        .setExpiration(new Date(now + this.expires))
        .compact();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    List<SimpleGrantedAuthority> authorities = Arrays.stream(
            claims.get(AUTHORITY_KEY).toString().split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    // todo. filter에서 authentication 설정 제대로 해서 수정
    CustomUserDetails principal = new CustomUserDetails(claims.getSubject(), "");
    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {

      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {

      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {

      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {

      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }
}
