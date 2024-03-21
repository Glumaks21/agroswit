package ua.com.agroswit.dto.response.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.agroswit.dto.response.CategoryDTO;
import ua.com.agroswit.model.Category;

import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public class CategoryDTOConverter implements Function<Category, CategoryDTO> {

    private final SubCategoryDTOConverter converter;

    @Override
    public CategoryDTO apply(Category category) {
        return new CategoryDTO(category.getId(),
                category.getName(),
                category.getSubCategories().stream().map(converter).collect(toSet())
        );
    }
}
