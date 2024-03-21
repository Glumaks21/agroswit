package ua.com.agroswit.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegistrationDTO(
        @NotBlank
        @Length(min = 6, max = 30)
        String login,

        @NotBlank
        @Length(min = 10, max = 30)
        String password
) {
}
