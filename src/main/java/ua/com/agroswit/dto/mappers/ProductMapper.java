package ua.com.agroswit.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.agroswit.dto.request.ProductCreationDTO;
import ua.com.agroswit.dto.ProductDTO;
import ua.com.agroswit.model.Product;

@Mapper(componentModel = "spring", uses = ProductPropertyValueMapper.class)
public interface ProductMapper {


    @Mapping(target = "categoryId", source = "category.id")
    ProductDTO toDTO(Product product);

    Product toEntity(ProductCreationDTO dto);
}
