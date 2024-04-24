package ua.com.agroswit.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDTO {

    private String email;

    @Size(min = 9, max = 12)
    private String phone;

    @Size(min = 6, max = 16)
    @NotBlank(message = "Password is required")
    private String password;

}
