package ua.com.agroswit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(description = "User sign up credentials")
public record RegistrationRequest(
        @NotBlank
        @Email
        @Length(min = 6, max = 320)
        String email,

        @NotBlank
        @Length(min = 9, max = 12)
        String phone,

        @NotBlank
        @Length(min = 10, max = 30)
        String password
) {
}
