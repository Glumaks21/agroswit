package ua.com.agroswit.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDTO {

    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 symbols")
    private String name;

    @Size(min = 2, max = 30, message = "Surname must be between 2 and 30 symbols")
    private String surname;

    @Size(min = 2, max = 30, message = "Patronymic must be between 2 and 30 symbols")
    private String patronymic;

    private String email;

    private String phone;

    @Size(min = 6, max = 16)
    @NotBlank(message = "Password is required")
    private String password;

}
