package ua.com.agroswit.productservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ua.com.agroswit.productservice.dto.validation.Groups;

import java.util.Set;

public record ProductPropertyGroupDTO(
        @Size(min = 2, max = 50, groups = Groups.Full.class, message = "Name must be between 2 and 50")
        @NotBlank(groups = Groups.Full.class, message = "Name must not be blank")
        String name,

        @Valid
        @NotEmpty(groups = Groups.Full.class, message = "Properties must not be empty")
        Set<ProductPropertyDTO> properties
) {
    public ProductPropertyGroupDTO(String name, Set<ProductPropertyDTO> properties) {
        this.name = name != null ? name.trim() : null;
        this.properties = properties;
    }
}
