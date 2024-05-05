package ua.com.agroswit.userdetails.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ua.com.agroswit.userdetails.model.enums.Role;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

public record UserServiceDTO(
        @JsonProperty(access = WRITE_ONLY)
        Integer id,
        String name,
        String surname,
        String patronymic,
        @JsonProperty(access = WRITE_ONLY)
        Role role,
        String email,
        String phone
) {
}