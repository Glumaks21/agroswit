package ua.com.agroswit.dto.response;

import lombok.Builder;
import ua.com.agroswit.model.PropertyType;

import java.util.Collection;

@Builder
public record CategoryDTO(
        Integer id,
        String name,
        String description,
        Integer parentCategoryId,
        Collection<PropertyDTO> properties) {

    public record PropertyDTO(String name, PropertyType type) {}
}
