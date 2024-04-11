package ua.com.agroswit.productservice.dto;

import jakarta.validation.constraints.*;

import java.util.Set;

public record SimplifiedProductDTO(
        @Size(min = 2, max = 30)
        String name,

        @Positive
        @NotNull
        Integer producerId,

        Integer article1CId,

        @Positive
        @NotNull
        Integer categoryId,

        @NotEmpty
        Set<ProductDTO.PackageDTO> packages
) {
}
