package ua.com.agroswit.dto;

import ua.com.agroswit.model.enums.RoleE;

public record UserDTO(
        Integer id,
        String email,
        String phone,
        RoleE role
) {
}
