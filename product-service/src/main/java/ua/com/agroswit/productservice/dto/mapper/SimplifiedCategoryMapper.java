package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.*;
import ua.com.agroswit.productservice.dto.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.model.Category;

@Mapper(componentModel = "spring")
public interface SimplifiedCategoryMapper {

    @Mapping(target = "parentCategoryName", source = "parentCategory.name")
    @Mapping(target = "logoUrl", expression = "java( logoUrl )")
    SimplifiedCategoryDTO toDTO(Category entity, @Context String logoUrl);

}
