package ua.com.agroswit.productservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import ua.com.agroswit.productservice.model.enums.MeasurementUnitE;

import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record FullProductDTO(
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

            @NotBlank(message = "Property name must not be blank")
            String name,

            @NotBlank(message = "Property value must not be blank")
            String value) {
    }

    public record PackageDTO(

            @NotNull(message = "Package count is required")
            @Positive(message = "Price must be positive")
            Double price,

            @NotNull(message = "Package count is required")
            @Positive(message = "Price must be positive")
            Integer count) {
    }
}
