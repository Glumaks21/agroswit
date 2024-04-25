package ua.com.agroswit.authservice.dto.response;

public record JwtResponse(
        String accessToken,
        String refreshToken
) {
}
