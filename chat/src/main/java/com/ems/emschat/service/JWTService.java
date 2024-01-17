package com.ems.emschat.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JWTService {
    private static final String SECRET_KEY = "2AC2B5D306B2868CDA4A5509084B286CC7560F1E7BA25AC2FE1EC99C46A06025";

    public String extractRole(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("role").toString();
    }

    public Integer extractId(String token) {
        final Claims claims = extractAllClaims(token);
        return (Integer) claims.get("id");
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
