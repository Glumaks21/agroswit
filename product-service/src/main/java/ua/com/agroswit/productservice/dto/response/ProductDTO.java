package ua.com.agroswit.productservice.dto.response;


import lombok.Builder;
import ua.com.agroswit.productservice.dto.request.ProducerDTO;
import ua.com.agroswit.productservice.dto.request.ProductModifiableDTO;
import ua.com.agroswit.productservice.model.enums.MeasurementUnitE;

import java.util.List;
import java.util.Set;

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
        Set<ProductModifiableDTO.PropertyDTO> properties,
        Set<ProductModifiableDTO.PackageDTO> packages,
        Integer categoryId) {
}
