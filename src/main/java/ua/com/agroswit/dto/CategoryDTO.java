package ua.com.agroswit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ua.com.agroswit.dto.Views;
import ua.com.agroswit.dto.Views.Create;
import ua.com.agroswit.dto.Views.Patch;
import ua.com.agroswit.model.enums.PropertyTypeE;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record CategoryDTO(

        @Positive
        Integer id,

        @JsonView({Create.class, Patch.class})
        @Size(min = 2, max = 30)
        String name,

        @JsonView({Create.class, Patch.class})
        @Size(min = 2, max = 300)
        String description,

        @JsonView({Create.class, Patch.class})
        @Positive
        Integer parentCategoryId,

        @JsonView({Create.class, Patch.class})
        List<PropertyDTO> properties,

        List<SimplifiedCategoryDTO> subcategories
) {

    public record PropertyDTO(
            @Size(min = 2, max = 50)
            String name,

            PropertyTypeE type
    ) {
    }
}
