package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public record PestDTO (
    @JsonProperty(access = READ_ONLY)
    Integer id,

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 symbols")
    @NotBlank(message = "Name must not be blank required")
    String name) {
}
