package ua.com.agroswit.productservice.dto.response;

public record SimpleDetailedProductDTO(
        Integer id,
        String imageUrl,
        String name,
        String shortDescription,
        String producer,
        Boolean active,
        Boolean inStock,
        Integer article1CId,
        Double price
) {
}
