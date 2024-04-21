package ua.com.agroswit.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InventoryDTO {

    @JsonView({Views.Create.class})
    @NotNull(groups = Groups.Create.class, message = "Article 1C id is required")
    private Integer article1CId;

    @JsonView({Views.Create.class})
    @NotNull(groups = Groups.Create.class, message = "Product id is required")
    private Integer productId;

    @JsonView({Views.Create.class, Views.Update.class})
    @Positive(groups = {Groups.Create.class, Groups.Update.class}, message = "Quantity must be positive")
    @NotNull(groups = {Groups.Create.class, Groups.Update.class}, message = "Quantity is required")
    private Integer quantity;

}
