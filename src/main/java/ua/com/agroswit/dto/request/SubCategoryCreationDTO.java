package ua.com.agroswit.dto.request;

import ua.com.agroswit.dto.response.SubCategoryPropertyDTO;

import java.util.Collection;

public record SubCategoryCreationDTO(
        String name,
        Collection<SubCategoryPropertyDTO> properties
) {
}
