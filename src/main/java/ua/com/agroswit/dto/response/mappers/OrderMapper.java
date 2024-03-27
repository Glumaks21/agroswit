package ua.com.agroswit.dto.response.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.agroswit.dto.response.OrderDTO;
import ua.com.agroswit.model.Order;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    OrderDTO toDTO(Order order);

}
