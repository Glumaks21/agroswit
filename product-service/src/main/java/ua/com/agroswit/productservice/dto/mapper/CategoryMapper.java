package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.model.Category;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;


@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    CategoryDTO toDTO(Category category);

    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "properties",
            source = "properties",
            nullValueCheckStrategy = ALWAYS,
            defaultExpression = "java(new java.util.HashSet<>())")
    Category toEntity(CategoryDTO dto);

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    void update(CategoryDTO dto, @MappingTarget Category entity);

}
