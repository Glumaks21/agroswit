package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ua.com.agroswit.productservice.dto.response.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.dto.validation.Groups;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryDTO(
        @JsonProperty(access = READ_ONLY)
        Integer id,

        @JsonProperty(access = READ_ONLY)
        String logoUrl,

        @Size(groups = {Groups.Full.class}, min = 2, max = 50, message = "Name length must be between 2 and 50")
        @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must not be blank")
        String name,

        @Size(groups = {Groups.Full.class}, min = 2, max = 300, message = "Description must be between 2 and 300")
        String description,

        Integer parentCategoryId,

        @Valid
        Set<CategoryPropertyGroupDTO> propertyGroups,

        @JsonProperty(access = READ_ONLY)
        Set<SimplifiedCategoryDTO> subcategories,

        Set<Integer> filterIds
) {

    public CategoryDTO(Integer id,
                       String logoUrl,
                       String name,
                       String description,
                       Integer parentCategoryId,
                       Set<CategoryPropertyGroupDTO> propertyGroups,
                       Set<SimplifiedCategoryDTO> subcategories,
                       Set<Integer> filterIds) {
        this.id = id;
        this.logoUrl = logoUrl != null ? logoUrl.trim() : null;
        this.name = name != null ? name.trim() : null;
        this.description = description != null ? description.trim() : null;
        this.parentCategoryId = parentCategoryId;
        this.propertyGroups = propertyGroups;
        this.subcategories = subcategories;
        this.filterIds = filterIds;
    }
}

