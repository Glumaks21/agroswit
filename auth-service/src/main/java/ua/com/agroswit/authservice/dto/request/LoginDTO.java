package ua.com.agroswit.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ua.com.agroswit.authservice.dto.validation.constraint.ValidPassword;

public record LoginDTO(

        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email address are not valid")
        String email,

        @Size(min = 9, max = 12)
        String phone,

        @ValidPassword
        @NotBlank(message = "Password must not be blank")
        String password,

        @Valid
        @JsonUnwrapped
        RequestMeta meta) {

    @JsonCreator
    public LoginDTO(String email, String phone, String password, String ip, String footprint) {
        this(email, phone, password, new RequestMeta(ip, footprint));
    }

    public LoginDTO(String email, String phone, String password, RequestMeta meta) {
        this.email = email != null ? email.trim() : null;
        this.phone = phone != null ? phone.trim() : null;
        this.password = password != null ? password.trim() : null;
        this.meta = meta;
    }
}
