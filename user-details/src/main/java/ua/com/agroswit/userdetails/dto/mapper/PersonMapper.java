package ua.com.agroswit.userdetails.dto.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.userdetails.dto.request.CompanyModifyingDTO;
import ua.com.agroswit.userdetails.dto.request.PersonModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.PersonDTO;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.model.Company;
import ua.com.agroswit.userdetails.model.Person;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = CustomerMapper.class)
public interface PersonMapper {
    @Mapping(target = "customer", source = ".")
    PersonDTO toDTO(Person entity, @Context UserServiceDTO user);

    @Mapping(source = "customer", target = ".")
    Person toEntity(PersonModifyingDTO dto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "customer", target = ".")
    void update(PersonModifyingDTO dto, @MappingTarget Person person);
}
