package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public record ProducerDTO (

        @JsonProperty(access = READ_ONLY)
        Integer id,

        @Size(min = 2, max = 100, message = "Name must be between 2 and 100")
        @NotBlank(message = "Name must not be blank")
        String name,

        @JsonProperty(access = READ_ONLY)
        String logoUrl

) {
}
