package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ua.com.agroswit.productservice.model.enums.PropertyTypeE;

import java.util.List;
import java.util.Set;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record CategoryDTO(

        @Positive
        Integer id,

        @JsonView({Views.Create.class, Views.Patch.class})
        @NotBlank(groups = {Groups.Create.class})
        @Size(min = 2, max = 30)
        String name,

        @JsonView({Views.Create.class, Views.Patch.class})
        @Size(min = 2, max = 300)
        String description,

        @JsonView({Views.Create.class, Views.Patch.class})
        @Positive
        Integer parentCategoryId,

        @JsonView({Views.Create.class, Views.Patch.class})
        List<PropertyDTO> properties,

        Set<SimplifiedCategoryDTO> subcategories
) {

    public record PropertyDTO(
            @JsonView({Views.Patch.class})
            @Positive
            Integer id,

            @JsonView({Views.Create.class, Views.Patch.class})
            @NotBlank
            @Size(min = 2, max = 50)
            String name,

            @JsonView({Views.Create.class, Views.Patch.class})
            @NotNull
            PropertyTypeE type
    ) {
    }


}

