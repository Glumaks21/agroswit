package ua.com.agroswit.productservice.dto;


import lombok.Builder;
import ua.com.agroswit.productservice.model.enums.MeasurementUnitE;
import ua.com.agroswit.productservice.model.enums.PropertyTypeE;

import java.util.Collection;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ProductDTO(
        Integer id,
        String imageUrl,
        String name,
        String description,
        ProducerDTO producer,
        Integer volume,
        MeasurementUnitE unit,
        Boolean active,
        Integer article1CId,
        List<ProductPropertyDTO> properties,
        List<PackageDTO> packages,
        Integer categoryId) {

    public record ProductPropertyDTO(
            String name,
            PropertyTypeE type,
            String value) {
    }

    public record PackageDTO(
            Double price,
            Integer count) {
    }
}
