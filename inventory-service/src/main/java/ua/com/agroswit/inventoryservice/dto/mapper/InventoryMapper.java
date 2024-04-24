package ua.com.agroswit.inventoryservice.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.inventoryservice.dto.InventoryDetailedDTO;
import ua.com.agroswit.inventoryservice.dto.CatalogServiceProductDTO;
import ua.com.agroswit.inventoryservice.dto.InventoryDTO;
import ua.com.agroswit.inventoryservice.model.Inventory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryDTO toDTO(Inventory entity);
    List<InventoryDTO> toDTO(List<Inventory> entities);

    @Mapping(target = ".", source = "pdto")
    InventoryDetailedDTO toDetailedDTO(Inventory entity, CatalogServiceProductDTO pdto);

    Inventory toEntity(InventoryDTO dto);

    @Mapping(target = "article1CId", ignore = true)
    @Mapping(target = "productId", ignore = true)
    void update(InventoryDTO dto, @MappingTarget Inventory entity);

}
