package ua.com.agroswit.security;

public record JwtResponseDTO(
        String accessToken,
        String refreshToken
) {
}
