package ua.com.agroswit.inventoryservice;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryDTO toDTO(Inventory entity);
    Inventory toEntity(InventoryDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "productId", ignore = true)
    void update(InventoryDTO dto, @MappingTarget Inventory entity);

}
