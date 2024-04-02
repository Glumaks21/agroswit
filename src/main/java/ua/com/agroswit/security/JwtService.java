package ua.com.agroswit.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ua.com.agroswit.model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final String secret;
    private final Integer expiration;

    public JwtService(JwtConfigurationProperties properties) {
        this.secret = properties.getSecret();
        this.expiration = properties.getExpiration();
    }

    public String generate(User user) {
        var issueDate = new Date();
        var expireDate = new Date(System.currentTimeMillis() + expiration);

        return Jwts.builder()
                .signWith(getKey())
                .subject(user.getEmail())
                .issuedAt(issueDate)
                .expiration(expireDate)
                .compact();
    }

    public String extractUserLogin(String jwt) {
        return extractClaims(jwt).getSubject();
    }

    public boolean isValid(String jwt, UserDetails userDetails) {
        var claims = extractClaims(jwt);
        var expiration = claims.getExpiration();
        var subject = claims.getSubject();

        return userDetails.getUsername().equals(subject)
                && expiration.after(new Date());
    }

    private boolean isTokenExpired(String jwt) {
        return extractClaims(jwt).getExpiration()
                .after(new Date());
    }

    private Claims extractClaims(String jwt) {
        return Jwts.parser()
                .decryptWith(getKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private SecretKey getKey() {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }
}
