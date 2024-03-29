package org.enesp.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class JwtService {
    // :TODO: Implement refresh token
    private static String SECRET;

    private static long EXPIRATION;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        SECRET = secret;
    }

    @Value("${jwt.expiration}")
    public void setExpiration(long expiration) {
        EXPIRATION = expiration;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Claims validateToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).build().parseSignedClaims(token).getPayload();
    }

}
