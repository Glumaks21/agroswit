package ua.com.agroswit.orderservice.dto.mapper;

import org.mapstruct.Mapper;
import ua.com.agroswit.orderservice.dto.OrderDTO;
import ua.com.agroswit.orderservice.model.Order;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = OrderItemMapper.class)
public interface OrderMapper {
    OrderDTO toDTO(Order order);
    List<OrderDTO> toDTOs(List<Order> orders);
    Order toEntity(OrderDTO dto);
}
