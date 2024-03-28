package ua.com.agroswit.dto.mappers;

import org.mapstruct.*;
import ua.com.agroswit.dto.CategoryDTO;
import ua.com.agroswit.model.Category;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    CategoryDTO toDTO(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "subcategories", source = "subcategories", ignore = true)
    void updateFromDTO(CategoryDTO dto, @MappingTarget Category entity);

    Category toEntity(CategoryDTO dto);

}
