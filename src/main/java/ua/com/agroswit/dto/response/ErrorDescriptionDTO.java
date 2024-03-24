package ua.com.agroswit.dto.response;


import java.util.Map;

public record ErrorDescriptionDTO(
        String reason,
        Map<String, String> errors,
        int code
) {
}
