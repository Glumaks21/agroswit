package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.*;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.CategoryDTO.PropertyDTO;
import ua.com.agroswit.productservice.model.Category;
import ua.com.agroswit.productservice.model.CategoryProperty;

import java.util.ArrayList;
import java.util.List;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;


@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    @Mapping(target = "logoUrl", expression = "java( logoUrl )")
    CategoryDTO toDTO(Category category, @Context String logoUrl);

    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "properties",
            source = "properties",
            nullValueCheckStrategy = ALWAYS,
            nullValuePropertyMappingStrategy = IGNORE)
    Category toEntity(CategoryDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "properties",
            qualifiedByName = "updateProperties",
            nullValueCheckStrategy = ALWAYS,
            nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "subcategories", ignore = true)
    void update(CategoryDTO dto, @MappingTarget Category entity);

    @Named("updateProperties")
    default void updateProperties(List<PropertyDTO> dtoProps, @MappingTarget List<CategoryProperty> entityProps) {
        var newProps = new ArrayList<CategoryProperty>(entityProps.size());
        for (var pdto : dtoProps) {
            CategoryProperty newProp = new CategoryProperty(null, pdto.name(), null);
            for (var prop : entityProps) {
                if (prop.getName().equals(pdto.name())) {
                    newProp = prop;
                    break;
                }
            }
            newProps.add(newProp);
        }
        System.out.println(newProps);
        entityProps.clear();
        entityProps.addAll(newProps);
    }

    @AfterMapping
    default void setPropsCategory(CategoryDTO dto, @MappingTarget Category entity) {
        entity.getProperties().forEach(p -> p.setCategory(entity));
    }
}
