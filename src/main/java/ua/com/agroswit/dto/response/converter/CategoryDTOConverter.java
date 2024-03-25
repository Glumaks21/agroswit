package ua.com.agroswit.dto.response.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.agroswit.dto.response.CategoryDTO;
import ua.com.agroswit.dto.response.CategoryDTO.PropertyDTO;
import ua.com.agroswit.model.Category;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryDTOConverter implements Function<Category, CategoryDTO> {

    private final SimplifiedCategoryDTOConverter simpleConverter;


    @Override
    public CategoryDTO apply(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentCategoryId(category.getParentCategory() != null
                        ? category.getParentCategory().getId()
                        : null
                )
                .subcategories(category.getSubcategories().stream()
                        .map(simpleConverter)
                        .collect(Collectors.toSet()))
                .properties(category.getProperties().stream()
                        .map(p -> new PropertyDTO(p.getName(), p.getType()))
                        .toList()
                )
                .build();
    }
}
