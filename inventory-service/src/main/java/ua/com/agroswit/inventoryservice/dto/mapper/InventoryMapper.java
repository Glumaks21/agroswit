package ua.com.agroswit.inventoryservice.dto.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.inventoryservice.dto.InventoryDTO;
import ua.com.agroswit.inventoryservice.dto.ProductDTO;
import ua.com.agroswit.inventoryservice.model.Inventory;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryDTO toDTO(Inventory entity);

    Inventory toEntity(InventoryDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "product1CId", ignore = true)
    void update(InventoryDTO dto, @MappingTarget Inventory entity);

}
