package ua.com.agroswit.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ua.com.agroswit.model.MeasurementUnit;
import ua.com.agroswit.model.Producer;
import ua.com.agroswit.model.PropertyType;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ProductDTO(
        Integer id,
        String imageUrl,
        String name,
        String description,
        Producer producer,
        Integer article1CId,
        Collection<ProductPropertyDTO> properties,
        Collection<PackageDTO> packages,
        Integer subcategoryId) {
    public record ProductPropertyDTO(String name, PropertyType type, String value) {
    }

    public record PackageDTO(
            Double price,
            Integer volume,
            MeasurementUnit unit) {
    }
}
