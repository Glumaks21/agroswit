package ua.com.agroswit.productservice.dto.request;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.com.agroswit.productservice.dto.validation.Groups;

import java.util.Set;

@Data
public class CategoryModifiableDTO {

    @Size(groups = {Groups.Create.class, Groups.FullUpdate.class}, min = 2, max = 50, message = "Name length must be between 2 and 50")
    @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must not be blank")
    private String name;

    @Size(groups = {Groups.Create.class, Groups.FullUpdate.class}, min = 2, max = 300, message = "Description must be between 2 and 300")
    private String description;

    private Integer parentCategoryId;

    @Valid
    private Set<PropertyGroupDTO> propertyGroups;

    private Set<Integer> filterIds;


    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            name = name.trim();
        }
        this.name = name;
    }

    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            description = description.trim();
        }
        this.description = description;
    }

    @Data
    @JsonView({Views.Create.class, Views.Update.class})
    public static class PropertyGroupDTO {

        @Size(min = 2, max = 50, groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must be between 2 and 50")
        @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must not be blank")
        private String name;

        @NotEmpty(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Properties must not be empty")
        private Set<String> properties;


        public void setName(String name) {
            if (name != null && !name.isEmpty()) {
                name = name.trim();
            }
            this.name = name;
        }
    }
}
