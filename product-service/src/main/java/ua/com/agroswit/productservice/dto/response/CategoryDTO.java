package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ua.com.agroswit.productservice.dto.validation.constraint.NameUnique;

import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView({Views.Detailed.class})
@Builder
public record CategoryDTO(

        @JsonProperty(access = READ_ONLY)
        Integer id,

        @JsonProperty(access = READ_ONLY)
        String logoUrl,

        @JsonView({Views.Detailed.class, Views.Create.class, Views.Update.class})
        @Size(groups = {Groups.Create.class, Groups.Update.class}, min = 2, max = 50, message = "Name length must be between 2 and 50")
        @NotBlank(groups = {Groups.Create.class, Groups.Update.class}, message = "Name must not be blank")
        String name,

        @JsonView({Views.Detailed.class, Views.Create.class, Views.Update.class})
        @Size(groups = {Groups.Create.class, Groups.Update.class}, min = 2, max = 300, message = "Description must be between 2 and 300")
        String description,

        @JsonView({Views.Detailed.class, Views.Create.class, Views.Update.class})
        Integer parentCategoryId,

        @JsonView({Views.Detailed.class, Views.Create.class, Views.Update.class})
//        @NameUnique(groups = {Groups.Create.class, Groups.Update.class})
        List<PropertyDTO> properties,

        @JsonProperty(access = READ_ONLY)
        List<SimplifiedCategoryDTO> subcategories
) {
    public record PropertyDTO(
            @JsonView({Views.Detailed.class, Views.Create.class, Views.Update.class})
            @Size(min = 2, max = 50, message = "Name must be between 2 and 50")
            @NotBlank(message = "Message must not be blank")
            String name
    ) {
    }
}

