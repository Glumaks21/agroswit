package ua.com.agroswit.dto.mappers;

import org.mapstruct.Mapper;
import ua.com.agroswit.dto.CustomerDTO;
import ua.com.agroswit.model.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO mapToDTO(Customer entity);

    Customer mapToEntity(CustomerDTO dto);
}
