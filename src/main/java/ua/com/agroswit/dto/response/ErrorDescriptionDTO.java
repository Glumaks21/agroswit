package ua.com.agroswit.dto.response;


public record ErrorDescriptionDTO(
        String reason,
        int code
) {
}
