package ua.com.agroswit.authservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.agroswit.authservice.config.JwtProperties;
import ua.com.agroswit.authservice.model.UserDetailsImpl;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.Jwts.SIG.HS256;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProps;


    public boolean isValid(String token, UserDetailsImpl user) {
        return user.email().equals(getSubject(token))
                && getExpirationDate(token).after(new Date());
    };

    public String extractEmail(String token) {
        return getSubject(token);
    }

    private String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    private Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generate(UserDetailsImpl user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .claim("role", user.role())
                .expiration(new Date(System.currentTimeMillis() + jwtProps.accessExpiration() * 1000L))
                .signWith(getKey(), HS256)
                .compact();
    }

    private SecretKey getKey() {
        byte[] bytes = Base64.getDecoder().decode(jwtProps.secret());
        return Keys.hmacShaKeyFor(bytes);
    }
}
