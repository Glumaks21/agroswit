package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ua.com.agroswit.productservice.dto.validation.constraint.NameUnique;
import ua.com.agroswit.productservice.model.enums.PropertyTypeE;

import java.util.Set;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record CategoryDTO(

        @JsonView({Views.Public.class})
        Integer id,

        @JsonView({Views.Public.class, Views.Create.class, Views.Update.class})
        @Size(groups = {Views.Create.class, Views.Update.class}, min = 2, max = 50, message = "Name length must be between 2 and 50")
        @NotBlank(groups = {Views.Create.class, Views.Update.class}, message = "Name must not be blank")
        String name,

        @JsonView({Views.Public.class, Views.Create.class, Views.Update.class})
        @Size(groups = {Views.Create.class, Views.Update.class}, min = 2, max = 300, message = "Description must be between 2 and 300")
        @NotBlank(groups = {Views.Create.class, Views.Update.class}, message = "Description must not be blank")
        String description,

        @JsonView({Views.Public.class, Views.Create.class, Views.Update.class})
        Integer parentCategoryId,

        @JsonView({Views.Public.class, Views.Create.class, Views.Update.class})
        @NameUnique(groups = {Groups.Create.class, Groups.Update.class})
        Set<PropertyDTO> properties,

        @JsonView(Views.Public.class)
        Set<SimplifiedCategoryDTO> subcategories

) {

    public record PropertyDTO(
            @Size(min = 2, max = 50, message = "Name must be between 2 and 50")
            @NotBlank(message = "Message must not be blank")
            String name,

            @NotNull(message = "Type is required")
            PropertyTypeE type
    ) {
    }
}

