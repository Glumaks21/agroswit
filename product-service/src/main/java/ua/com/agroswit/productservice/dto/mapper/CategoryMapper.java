package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.model.Category;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    CategoryDTO toDTO(Category category);
    Category toEntity(CategoryDTO dto);
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "subcategories", source = "subcategories", ignore = true)
    void update(CategoryDTO dto, @MappingTarget Category entity);

}
