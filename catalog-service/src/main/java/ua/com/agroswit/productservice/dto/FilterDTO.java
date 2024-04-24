package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public record FilterDTO (
    @JsonProperty(access = READ_ONLY)
    Integer id,

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 symbols")
    @NotBlank(message = "Name is required")
    String name,

    Integer parentFilterId,

    @JsonProperty(access = READ_ONLY)
    Set<FilterDTO> children) {
}
