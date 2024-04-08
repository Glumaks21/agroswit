package ua.com.agroswit.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InventoryDTO {

    @JsonView({Views.Public.class, Views.Create.class})
    @NotNull(groups = Groups.Create.class, message = "Id is required")
    private Integer product1CId;

    @JsonView(Views.Public.class)
    @JsonUnwrapped
    private ProductDTO product;

    @JsonView({Views.Public.class, Views.Create.class, Views.Update.class})
    @Positive(groups = {Groups.Create.class, Groups.Update.class}, message = "Quantity must be positive")
    @NotNull(groups = {Groups.Create.class, Groups.Update.class}, message = "Quantity is required")
    private Integer quantity;

}
