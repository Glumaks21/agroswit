package ua.com.agroswit.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.agroswit.dto.OrderDTO;
import ua.com.agroswit.model.Order;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "state", source = "state.name")
    OrderDTO toDTO(Order order);

}
