package ua.com.agroswit.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import ua.com.agroswit.dto.response.ProductDTO;

import java.util.Set;

public record ProductCreationDTO(
        @NotBlank
        @Size(min = 2, max = 30)
        String name,

        @Positive
        @NotNull
        Integer producerId,

        @Positive
        @NotNull
        Integer categoryId,

        @NotEmpty
        Set<ProductDTO.PackageDTO> packages
) {
}
