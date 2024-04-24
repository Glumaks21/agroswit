package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.*;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.response.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.model.Category;
import ua.com.agroswit.productservice.model.Filter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;


@Mapper(nullValueCheckStrategy = ALWAYS, nullValueIterableMappingStrategy = RETURN_DEFAULT, componentModel = "spring")
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

    Category toEntity(CategoryDTO dto);

    @AfterMapping
    default void configureRelations(CategoryDTO dto, @MappingTarget Category entity) {
        entity.getPropertyGroups().forEach(g -> g.setCategory(entity));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    void update(CategoryDTO dto, @MappingTarget Category entity);

}
