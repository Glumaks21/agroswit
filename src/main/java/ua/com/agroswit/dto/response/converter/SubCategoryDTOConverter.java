package ua.com.agroswit.dto.response.converter;

import org.springframework.stereotype.Component;
import ua.com.agroswit.dto.response.SubCategoryDTO;
import ua.com.agroswit.model.SubCategory;

import java.util.function.Function;

@Component
public class SubCategoryDTOConverter implements Function<SubCategory, SubCategoryDTO> {

    @Override
    public SubCategoryDTO apply(SubCategory subCategory) {
        return new SubCategoryDTO(subCategory.getId(), subCategory.getName());
    }
}
