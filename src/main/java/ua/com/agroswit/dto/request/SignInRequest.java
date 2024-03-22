package ua.com.agroswit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(description = "User sign in credentials")
public record SignInRequest(
        @NotBlank
        @Length(min = 6, max = 30)
        String login,

        @NotBlank
        @Length(min = 6, max = 20)
        String password
) {
}
