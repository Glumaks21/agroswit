package ua.com.agroswit.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ua.com.agroswit.productservice.dto.validation.Groups;

import java.util.Set;

public record CategoryPropertyGroupDTO(
        @Size(min = 2, max = 50, groups = Groups.Full.class, message = "Name must be between 2 and 50")
        @NotBlank(groups = Groups.Full.class, message = "Name must not be blank")
        String name,

        @NotEmpty(groups = Groups.Full.class, message = "Properties must not be empty")
        Set<String> properties
) {
    public CategoryPropertyGroupDTO(String name,
                                    Set<String> properties) {
        this.name = name != null? name.trim(): null;
        this.properties = properties;
    }
}
