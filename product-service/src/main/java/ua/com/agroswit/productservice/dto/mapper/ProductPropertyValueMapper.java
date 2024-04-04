package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.model.ProductPropertyValue;

@Mapper(componentModel = "spring")
public interface ProductPropertyValueMapper {

    @Mapping(target = "type", source = "categoryProperty.type")
    @Mapping(target = "name", source = "categoryProperty.name")
    ProductDTO.ProductPropertyDTO toDTO(ProductPropertyValue value);
}
