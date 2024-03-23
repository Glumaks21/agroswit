package ua.com.agroswit.dto.response.converter;

import org.springframework.stereotype.Component;
import ua.com.agroswit.dto.response.SubCategoryDTO;
import ua.com.agroswit.dto.response.SubCategoryPropertyDTO;
import ua.com.agroswit.model.SubCategory;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SubCategoryDTOConverter implements Function<SubCategory, SubCategoryDTO> {

    @Override
    public SubCategoryDTO apply(SubCategory subCategory) {
        return SubCategoryDTO.builder()
                .id(subCategory.getId())
                .name(subCategory.getName())
                .categoryId(subCategory.getCategory().getId())
                .properties(subCategory.getProperties().stream()
                        .map(p -> new SubCategoryPropertyDTO(p.getName(), p.getType()))
                        .collect(Collectors.toSet())
                )
                .build();
    }
}
