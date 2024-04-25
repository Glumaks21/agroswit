package ua.com.agroswit.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import ua.com.agroswit.authservice.dto.validation.constraint.ValidPassword;

public record RegistrationDTO(

        @Size(min = 2, max = 30, message = "Name must be between 2 and 30 symbols")
        @NotBlank(message = "Name must not be blank")
        String name,

        @Size(min = 2, max = 30, message = "Surname must be between 2 and 30 symbols")
        @NotBlank(message = "Surname must not be blank")
        String surname,

        @Size(min = 2, max = 30, message = "Patronymic must be between 2 and 30 symbols")
        @NotBlank(message = "Patronymic must not be blank")
        String patronymic,

        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email address are not valid")
        @NotBlank(message = "Email must not be blank")
        String email,

        @NotBlank(message = "Phone must not be blank")
        String phone,

        @ValidPassword
        @NotBlank(message = "Password must not be blank")
        String password
) {
    public RegistrationDTO(String name,
                           String surname,
                           String patronymic,
                           String email, String phone,
                           String password) {
        this.name = name != null ? name.trim() : null;
        this.surname = surname != null ? surname.trim() : null;
        this.patronymic = patronymic != null ? patronymic.trim() : null;
        this.email = email != null ? email.trim() : null;
        this.phone = phone != null ? phone.trim() : null;
        this.password = password != null ? password.trim() : null;
    }
}
