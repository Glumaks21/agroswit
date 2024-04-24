package ua.com.agroswit.authservice.dto.mapper;

import org.mapstruct.Mapper;
import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.response.UserDTO;
import ua.com.agroswit.authservice.model.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(RegistrationDTO dto);
}
