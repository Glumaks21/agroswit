package ua.com.agroswit.inventoryservice.dto;



public record ProductServiceProductDTO(
        Integer id,
        String name,
        String imageUrl,
        Integer volume,
        String unit
) {
}
