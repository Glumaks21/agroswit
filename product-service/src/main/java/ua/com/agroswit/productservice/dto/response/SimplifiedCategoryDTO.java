package ua.com.agroswit.productservice.dto;



public record SimplifiedCategoryDTO(
        Integer id,
        String logoUrl,
        String name,
        String description,
        String parentCategoryName
) {
}
