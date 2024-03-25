package ua.com.agroswit.dto.response.converter;

import org.springframework.stereotype.Component;
import ua.com.agroswit.dto.response.SimplifiedCategoryDTO;
import ua.com.agroswit.model.Category;

import java.util.function.Function;

@Component
public class SimplifiedCategoryDTOConverter implements Function<Category, SimplifiedCategoryDTO> {
    @Override
    public SimplifiedCategoryDTO apply(Category category) {
        return new SimplifiedCategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
