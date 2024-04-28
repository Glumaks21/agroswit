package ua.com.agroswit.productservice.dto.response;

import ua.com.agroswit.productservice.dto.enums.ProductState;

public record SimpleDetailedProductDTO(
        Integer id,
        String imageUrl,
        String name,
        String shortDescription,
        String producer,
        Boolean active,
        Boolean inStock,
        ProductState state,
        Integer article1CId,
        Double price
) {
}
