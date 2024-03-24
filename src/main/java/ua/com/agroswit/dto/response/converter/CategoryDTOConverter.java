package ua.com.agroswit.dto.response.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.agroswit.dto.response.CategoryDTO;
import ua.com.agroswit.model.Category;
import ua.com.agroswit.model.CategoryProperty;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CategoryDTOConverter implements Function<Category, CategoryDTO> {

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
                .properties(category.getProperties().stream()
                        .map(c -> new CategoryDTO.PropertyDTO(c.getName(), c.getType()))
                        .toList()
                )
                .build();
    }
}
