package ua.com.agroswit.orderservice.dto.mapper;

import org.mapstruct.Mapper;
import ua.com.agroswit.orderservice.dto.OrderHistoryDTO;
import ua.com.agroswit.orderservice.model.OrderHistory;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OrderHistoryMapper {
    OrderHistoryDTO toDTO(OrderHistory entity);
    List<OrderHistoryDTO> toDTOs(List<OrderHistory> entities);
}
