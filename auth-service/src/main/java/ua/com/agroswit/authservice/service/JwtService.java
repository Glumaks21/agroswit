package ua.com.agroswit.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.agroswit.authservice.config.JwtConfigProperties;
import ua.com.agroswit.authservice.model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfigProperties jwtProps;


    public boolean isValid(String token, User user) {
        return user.getEmail().equals(getSubject(token))
                && getExpirationDate(token).before(new Date());
    };

    private String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    private Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .decryptWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .signWith(getKey())
                .subject(user.getEmail())
                .issuedAt(new Date())
                .claim("role", user.getRole())
                .expiration(new Date(System.currentTimeMillis() + jwtProps.accessExpiration()))
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .signWith(getKey())
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProps.refreshExpiration()))
                .compact();
    }

    private SecretKey getKey() {
        byte[] bytes = jwtProps.secret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }
}
