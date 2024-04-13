package ua.com.agroswit.productservice.dto;


import lombok.Builder;
import ua.com.agroswit.productservice.model.enums.MeasurementUnitE;

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
        List<PropertyDTO> properties,
        List<PackageDTO> packages,
        Integer categoryId) {

    public record PropertyDTO(
            String name,
            String value) {
    }

    public record PackageDTO(
            Double price,
            Integer count) {
    }
}
