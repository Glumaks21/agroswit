package ua.com.agroswit.orderservice.dto.mapper;

import org.mapstruct.Mapper;
import ua.com.agroswit.orderservice.dto.OrderItemDTO;
import ua.com.agroswit.orderservice.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDTO toDTO(OrderItem entity);
}
