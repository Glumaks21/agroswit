package ua.com.agroswit.dto.request;

import jakarta.validation.constraints.*;
import ua.com.agroswit.dto.response.CategoryDTO.PropertyDTO;

import java.util.List;

public record CategoryCreationDTO(
        @NotBlank
        @Size(min = 2, max = 20)
        String name,

        @Size(min = 20, max = 300)
        String description,

        @DecimalMin("1")
        Integer parentCategoryId,

        @NotNull
        List<PropertyDTO> properties
) {
}
