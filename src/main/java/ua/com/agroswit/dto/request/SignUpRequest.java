package ua.com.agroswit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(description = "User sign up credentials")
public record SignUpRequest(
        @NotBlank
        @Length(min = 6, max = 30)
        String login,

        @NotBlank
        @Length(min = 10, max = 30)
        String password
) {
}
