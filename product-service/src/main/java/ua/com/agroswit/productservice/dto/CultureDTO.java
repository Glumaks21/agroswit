package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import ua.com.agroswit.productservice.dto.validation.Groups.Create;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
public class CultureDTO {

    @JsonProperty(access = READ_ONLY)
    private Integer id;

    @Size(min = 2, max = 50, groups = Create.class, message = "Culture name must be between 2 and 50 symbols")
    @NotBlank(groups = Create.class, message = "Culture name must not be blank")
    private String name;


    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            name = name.trim();
        }
        this.name = name;
    }
}
