package ua.com.agroswit.userdetails.dto.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.userdetails.dto.request.CompanyModifyingDTO;
import ua.com.agroswit.userdetails.dto.request.EntrepreneurModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.EntrepreneurDTO;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.model.Company;
import ua.com.agroswit.userdetails.model.Entrepreneur;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = CustomerMapper.class)
public interface EntrepreneurMapper {
    @Mapping(target = "customer", source = ".")
    EntrepreneurDTO toDTO(Entrepreneur entity, @Context UserServiceDTO user);

    @Mapping(source = "customer", target = ".")
    Entrepreneur toEntity(EntrepreneurModifyingDTO dto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "customer", target = ".")
    void update(CompanyModifyingDTO dto, @MappingTarget Company company);
}
