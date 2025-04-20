package com.insanet.insanet_backend.config;

import com.insanet.insanet_backend.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    @Value("${security.jwt.expiration}")
    private long jwtExpirationMs;

    public JwtTokenProvider(@Value("${security.jwt.secret}") String secret) {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("JWT Secret Key is not provided or is empty!");
        }
        // Secret key'i 64 byte'a tamamla (HS512 için minimum gereksinim)
        byte[] keyBytes = new byte[64];
        byte[] secretBytes = secret.getBytes();
        System.arraycopy(secretBytes, 0, keyBytes, 0, Math.min(secretBytes.length, keyBytes.length));
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, Set<Role> roles) {

        List<String> roleNames = roles.stream()
                .map(role -> "ROLE_" + role.getAuthority())
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNames)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", List.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT Token süresi dolmuş: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Geçersiz JWT token: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("JWT imza hatası: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Token doğrulanamadı: {}", e.getMessage());
        }
        return false;
    }
}
