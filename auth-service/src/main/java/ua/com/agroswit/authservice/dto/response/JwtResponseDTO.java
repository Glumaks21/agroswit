package ua.com.agroswit.authservice.dto.response;

public record JwtResponseDTO(
        String accessToken,
        String refreshToken
) {
}
