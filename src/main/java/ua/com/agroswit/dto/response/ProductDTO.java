package ua.com.agroswit.dto.response;


import lombok.Builder;
import ua.com.agroswit.model.enums.MeasurementUnitE;
import ua.com.agroswit.model.Producer;
import ua.com.agroswit.model.enums.PropertyTypeE;

import java.util.Collection;

//@JsonInclude(JsonInclude.Include.NON_NULL)
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
        Integer categoryId) {
    public record ProductPropertyDTO(String name, PropertyTypeE type, String value) {
    }

    public record PackageDTO(
            Double price,
            Integer volume,
            MeasurementUnitE unit) {
    }
}
