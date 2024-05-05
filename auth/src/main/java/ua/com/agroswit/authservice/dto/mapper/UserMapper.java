package ua.com.agroswit.authservice.dto.mapper;

import org.mapstruct.Mapper;
import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.response.UserDTO;
import ua.com.agroswit.authservice.model.User;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User entity);
    List<UserDTO> toDTOs(List<User> entities);
    User toEntity(RegistrationDTO dto);
}
