package ua.com.agroswit.orderservice.dto.mapper;

import org.mapstruct.Mapper;
import ua.com.agroswit.orderservice.dto.OrderItemDTO;
import ua.com.agroswit.orderservice.model.OrderItem;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OrderItemMapper {
    OrderItemDTO toDTO(OrderItem entity);
}
