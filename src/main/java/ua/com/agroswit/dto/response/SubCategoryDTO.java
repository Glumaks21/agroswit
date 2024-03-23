package ua.com.agroswit.dto.response;

import lombok.Builder;

import java.util.Collection;

@Builder
public record SubCategoryDTO(
        Integer id,
        String name,
        Integer categoryId,
        Collection<SubCategoryPropertyDTO> properties
) {
}
