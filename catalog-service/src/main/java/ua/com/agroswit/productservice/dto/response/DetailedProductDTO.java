package ua.com.agroswit.productservice.dto.response;


import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.ProductDTO.PackageDTO;
import ua.com.agroswit.productservice.dto.ProductDTO.PropertyGroupDTO;
import ua.com.agroswit.productservice.model.enums.MeasurementUnitE;
import ua.com.agroswit.productservice.model.enums.ProductType;

import java.util.Set;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public record DetailedProductDTO(
        Integer id,
        Integer article1CId,
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
        Boolean inStock,
        Set<PropertyGroupDTO> propertyGroups,
        Set<PackageDTO> packages,
        Integer categoryId,
        Set<Integer> filterIds) {
}
