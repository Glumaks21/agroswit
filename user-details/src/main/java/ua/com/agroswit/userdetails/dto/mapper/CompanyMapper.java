package ua.com.agroswit.userdetails.dto.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.userdetails.dto.response.CompanyDTO;
import ua.com.agroswit.userdetails.dto.request.CompanyModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.model.Company;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = CustomerMapper.class)
public interface CompanyMapper {

    @Mapping(target = "customer", source = ".")
    CompanyDTO toDTO(Company entity, @Context UserServiceDTO user);

    @Mapping(source = "customer", target = ".")
    Company toEntity(CompanyModifyingDTO dto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "customer", target = ".")
    void update(CompanyModifyingDTO dto, @MappingTarget Company company);
}
