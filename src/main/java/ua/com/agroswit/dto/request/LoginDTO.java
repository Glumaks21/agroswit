package ua.com.agroswit.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginDTO(
        @NotBlank
        @Length(min = 6, max = 30)
        String login,

        @NotBlank
        @Length(min = 6, max = 20)
        String password
) {
}
