package ua.com.agroswit.inventoryservice.dto;



public record CatalogServiceProductDTO(
        Integer id,
        String name,
        String imageUrl,
        Integer volume,
        String unit
) {
}
