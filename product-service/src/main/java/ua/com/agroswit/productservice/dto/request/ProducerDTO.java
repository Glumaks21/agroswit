package ua.com.agroswit.productservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ua.com.agroswit.productservice.dto.request.Views;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@JsonView({Views.Create.class, Views.Update.class})
public record ProducerDTO(
        @JsonProperty(access = READ_ONLY)
        Integer id,

        @Size(min = 2, max = 100, message = "Name must be between 2 and 100")
        @NotBlank(message = "Name must not be blank")
        String name,

        @JsonProperty(access = READ_ONLY)
        String logoUrl
) {
}
