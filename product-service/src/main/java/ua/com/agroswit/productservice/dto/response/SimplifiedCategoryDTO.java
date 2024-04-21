package ua.com.agroswit.productservice.dto.response;



public record SimplifiedCategoryDTO(
        Integer id,
        String logoUrl,
        String name,
        String parentCategoryName
) {
}
