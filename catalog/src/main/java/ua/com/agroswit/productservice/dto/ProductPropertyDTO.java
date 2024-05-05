package ua.com.agroswit.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ua.com.agroswit.productservice.dto.validation.Groups;

public record ProductPropertyDTO(
        @Size(min = 2, max = 50, groups = {Groups.Full.class}, message = "Name must be between 2 and 50")
        @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must not be blank")
        String name,

        @Size(max = 255, groups = {Groups.Full.class}, message = "Value must be lower then 255 symbols")
        @NotBlank(groups = {Groups.Full.class}, message = "Value must not be blank")
        String value) {

    public ProductPropertyDTO(String name,
                              String value) {
        this.name = name != null? name.trim(): null;
        this.value = value != null? value.trim(): null;
    }
}
