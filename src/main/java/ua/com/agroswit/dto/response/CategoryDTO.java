package ua.com.agroswit.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ua.com.agroswit.model.PropertyType;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record CategoryDTO(
        Integer id,
        String name,
        String description,
        Integer parentCategoryId,
        Collection<SimplifiedCategoryDTO> subcategories,
        Collection<PropertyDTO> properties
) {
    public record PropertyDTO(String name, PropertyType type) {
    }
}
