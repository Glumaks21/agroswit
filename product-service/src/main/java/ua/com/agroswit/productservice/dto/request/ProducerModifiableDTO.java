package ua.com.agroswit.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.com.agroswit.productservice.dto.validation.Groups.Create;


@Data
public class ProducerModifiableDTO {

    @Size(min = 2, max = 100, groups = {Create.class, Create.class}, message = "Name must be between 2 and 100")
    @NotBlank(groups = {Create.class, Create.class}, message = "Name must not be blank")
    private String name;


    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            name = name.trim();
        }
        this.name = name;
    }
}
