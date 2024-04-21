package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.*;
import ua.com.agroswit.productservice.dto.request.CategoryModifiableDTO;
import ua.com.agroswit.productservice.dto.request.CategoryModifiableDTO.PropertyGroupDTO;
import ua.com.agroswit.productservice.dto.response.CategoryDTO;
import ua.com.agroswit.productservice.dto.response.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.model.Category;
import ua.com.agroswit.productservice.model.CategoryPropertyGroup;
import ua.com.agroswit.productservice.model.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;


@Mapper(componentModel = "spring", uses = FilterMapper.class)
public interface CategoryMapper {

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    @Mapping(target = "filterIds", source = "filters")
    @Mapping(target = "logoUrl", expression = "java( logoUrl )")
    CategoryDTO toDTO(Category category, @Context String logoUrl);

    default Set<Integer> mapToFilterIds(List<Filter> filters) {
        return filters.stream()
                .map(Filter::getId)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "parentCategoryName", source = "parentCategory.name")
    @Mapping(target = "logoUrl", expression = "java( logoUrl )")
    SimplifiedCategoryDTO toSimplifiedDTO(Category entity, @Context String logoUrl);

    @Mapping(target = "propertyGroups",
            source = "propertyGroups",
            nullValueCheckStrategy = ALWAYS,
            nullValuePropertyMappingStrategy = IGNORE)
    Category toEntity(CategoryModifiableDTO dto);

    @AfterMapping
    default void configureRelations(CategoryModifiableDTO dto, @MappingTarget Category entity) {
        entity.getPropertyGroups().forEach(g -> g.setCategory(entity));
    }

    @Mapping(target = "propertyGroups",
            nullValueCheckStrategy = ALWAYS,
            nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "subcategories", ignore = true)
    void update(CategoryModifiableDTO dto, @MappingTarget Category entity);

}
