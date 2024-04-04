package ua.com.agroswit.orderservice.dto.mapper;

import org.mapstruct.Mapper;
import ua.com.agroswit.orderservice.dto.OrderDTO;
import ua.com.agroswit.orderservice.model.Order;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    OrderDTO toDTO(Order order);

}
