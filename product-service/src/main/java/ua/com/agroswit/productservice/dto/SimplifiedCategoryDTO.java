package ua.com.agroswit.productservice.dto;


import com.fasterxml.jackson.annotation.JsonView;

@JsonView({Views.Detailed.class})
public record SimplifiedCategoryDTO(

        @JsonView({Views.Short.class, Views.Detailed.class})
        Integer id,

        @JsonView({Views.Short.class, Views.Detailed.class})
        String logoUrl,

        @JsonView({Views.Short.class, Views.Detailed.class})
        String name,

        String description,

        String parentCategoryName
) {
}
