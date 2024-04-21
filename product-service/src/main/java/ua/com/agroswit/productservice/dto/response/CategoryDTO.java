package ua.com.agroswit.productservice.dto.response;

import ua.com.agroswit.productservice.dto.FilterDTO;

import java.util.Set;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryDTO(
        Integer id,
        String logoUrl,
        String name,
        String description,
        Integer parentCategoryId,
        Set<PropertyGroupDTO> propertyGroups,
        Set<SimplifiedCategoryDTO> subcategories,
        Set<Integer> filterIds) {

    public record PropertyGroupDTO(
            String name,
            Set<String> properties
    ) {
    }
}

