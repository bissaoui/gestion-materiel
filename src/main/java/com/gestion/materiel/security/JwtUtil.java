package com.gestion.materiel.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "yASSINEbISSAOUI199711231225Recardo@S10+";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());  // Utiliser la clé correctement

    public String generateToken(String cin, String role, String username) {
        return Jwts.builder()
                .subject(username)  // "sub" contient le username
                .claim("role", role) // Ajout du rôle
                .claim("cin", cin)   // Ajout du CIN
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expiration 1h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT: " + e.getMessage()); // Ajout de logs
            return false;
        }
    }
}
