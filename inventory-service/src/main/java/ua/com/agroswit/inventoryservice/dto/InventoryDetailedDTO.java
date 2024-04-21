package ua.com.agroswit.inventoryservice.dto;


public record InventoryDetailedDTO(
        Integer article1CId,
        Integer productId,
        String name,
        String imageUrl,
        Integer volume,
        String unit,
        Integer quantity
) {
}
