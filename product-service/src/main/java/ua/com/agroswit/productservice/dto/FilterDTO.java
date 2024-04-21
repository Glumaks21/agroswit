package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.com.agroswit.productservice.dto.validation.Groups;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
public class FilterDTO {

    @JsonProperty(access = READ_ONLY)
    private Integer id;

    @Size(min = 2, groups = {Groups.Create.class, Groups.FullUpdate.class}, max = 100, message = "Filter name must be between 2 and 100 symbols")
    @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Filter name is required")
    private String name;

    private Integer parentFilterId;

    @JsonProperty(access = READ_ONLY)
    private Set<FilterDTO> children;


    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            name = name.trim();
        }
        this.name = name;
    }
}
