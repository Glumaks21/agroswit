package ua.com.agroswit.dto.mappers;

import org.mapstruct.Mapper;
import ua.com.agroswit.dto.UserDTO;
import ua.com.agroswit.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User entity);
}
