package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProducerDTO(
        Integer id,

        @JsonView({Views.Create.class, Views.Patch.class})
        @Size(min = 2, max = 200, message = "Name must be between 2 and 300")
        @NotBlank(message = "Name must not be blank")
        String name,

        String logoUrl
) {
}
