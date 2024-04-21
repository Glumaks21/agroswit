package ua.com.agroswit.productservice.dto.response;

import ua.com.agroswit.productservice.dto.request.ProductModifiableDTO.PackageDTO;
import ua.com.agroswit.productservice.dto.request.ProductModifiableDTO.PropertyGroupDTO;
import ua.com.agroswit.productservice.model.enums.MeasurementUnitE;
import ua.com.agroswit.productservice.model.enums.ProductType;

import java.util.Set;

public record ProductDTO(
        Integer id,
        String imageUrl,
        String name,
        String shortDescription,
        String fullDescription,
        String recommendations,
        ProductType type,
        Integer producerId,
        Integer volume,
        MeasurementUnitE unit,
        Boolean active,
        Set<PropertyGroupDTO> propertyGroups,
        Set<PackageDTO> packages,
        Integer categoryId,
        Set<Integer> filterIds) {
}
