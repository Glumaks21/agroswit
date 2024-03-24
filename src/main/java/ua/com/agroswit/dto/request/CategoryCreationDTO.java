package ua.com.agroswit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ua.com.agroswit.dto.response.CategoryDTO;

import java.util.List;

public record CategoryCreationDTO(
        @NotBlank
        @Size(min = 2, max = 20)
        String name,

        @Size(min = 20, max = 300)
        String description,

        @NotNull
        List<CategoryDTO.PropertyDTO> properties
) {
}
