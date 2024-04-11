package ua.com.agroswit.inventoryservice.dto;


import com.fasterxml.jackson.annotation.JsonView;
import ua.com.agroswit.inventoryservice.dto.enums.MeasurementUnitE;

@JsonView(Views.Public.class)
public record ProductDTO(
        String name,
        String imageUrl,
        Integer volume,
        MeasurementUnitE unit,
        Boolean active
) {
}
