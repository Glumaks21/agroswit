package ua.com.agroswit.dto.response;

import ua.com.agroswit.model.SubCategoryPropertyTypeE;

public record SubCategoryPropertyDTO(
        String name,
        SubCategoryPropertyTypeE type
) {
}
