package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;


public record ProducerDTO(
        @JsonProperty(access = READ_ONLY)
        Integer id,

        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 symbols")
        @NotBlank(message = "Name must not be blank")
        String name,

        @Size(min = 10, max = 100, message = "Description must be between 10 and 100 symbols")
        String description,

        @JsonProperty(access = READ_ONLY)
        String logoUrl
) {
    public ProducerDTO(Integer id, String name, String description, String logoUrl) {
        this.id = id;
        this.name = name != null ? name.trim() : null;
        this.description = description != null ? description.trim() : null;
        this.logoUrl = logoUrl != null ? logoUrl.trim() : null;
    }
}
