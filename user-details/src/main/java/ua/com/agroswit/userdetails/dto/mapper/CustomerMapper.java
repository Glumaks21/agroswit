package ua.com.agroswit.userdetails.dto.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.agroswit.userdetails.dto.response.CustomerDTO;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.model.Customer;

import java.util.List;
import java.util.stream.IntStream;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CustomerMapper {
    @Mapping(target = "user", expression = "java( user )")
    CustomerDTO toDTO(Customer entity, @Context UserServiceDTO user);

    default List<CustomerDTO> toDTOs(List<Customer> customers, List<UserServiceDTO> users) {
        if (customers.size() != users.size()) {
            throw new IllegalArgumentException("Customers list must have same size as users");
        }
        return IntStream.range(0, customers.size())
                .mapToObj(i -> toDTO(customers.get(i), users.get(i)))
                .toList();
    }
}
