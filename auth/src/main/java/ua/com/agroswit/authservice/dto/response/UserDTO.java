package ua.com.agroswit.authservice.dto.response;

import ua.com.agroswit.authservice.model.enums.Role;

import java.time.LocalDateTime;

public record UserDTO(
        Integer id,
        String name,
        String surname,
        String patronymic,
        Role role,
        String email,
        String phone,
        LocalDateTime createdAt
) {
}
