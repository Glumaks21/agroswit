package ua.com.agroswit.productservice.dto.response;

public record InventoryServiceDTO(
        Integer article1CId,
        Integer productId,
        Integer quantity
) {
}
