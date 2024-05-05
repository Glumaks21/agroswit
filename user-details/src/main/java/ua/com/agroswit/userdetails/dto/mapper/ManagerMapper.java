package ua.com.agroswit.userdetails.dto.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.userdetails.dto.request.ManagerModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.ManagerDTO;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.model.Manager;


import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ManagerMapper {
    @Mapping(target = "user", expression = "java( user )")
    ManagerDTO toDTO(Manager entity, @Context UserServiceDTO user);
    Manager toEntity(ManagerModifyingDTO dto);
    @Mapping(target = "userId", ignore = true)
    void update(ManagerModifyingDTO dto, @MappingTarget Manager manager);
}
