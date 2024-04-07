package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.agroswit.productservice.dto.ProductCreationDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mapping(target = "categoryId", source = "category.id")
    ProductDTO toDTO(Product product);

    Product toEntity(ProductCreationDTO dto);
}
