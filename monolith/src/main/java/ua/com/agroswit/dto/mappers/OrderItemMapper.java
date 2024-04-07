package ua.com.agroswit.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.agroswit.dto.OrderDTO.OrderItemDTO;
import ua.com.agroswit.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "packageId", source = "productPackage.id")
    OrderItemDTO toDTO(OrderItem entity);
}
