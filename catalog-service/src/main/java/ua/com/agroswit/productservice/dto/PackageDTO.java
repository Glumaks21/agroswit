package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ua.com.agroswit.productservice.dto.request.Views;
import ua.com.agroswit.productservice.dto.validation.Groups;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@JsonView(Views.Full.class)
public record PackageDTO(

        @JsonView({Views.FullUpdate.class, Views.PartialUpdate.class})
        @NotNull(groups = {Groups.FullUpdate.class, Groups.PartialUpdate.class}, message = "Id is required")
        Integer id,

        @NotNull(groups = Groups.Full.class, message = "Price is required")
        @Positive(groups = Groups.Full.class, message = "Price must be positive")
        Double price,

        @JsonProperty(access = READ_ONLY)
        Double oldPrice,

        @NotNull(groups = Groups.Full.class, message = "Count is required")
        @Positive(groups = Groups.Full.class, message = "Count must be positive")
        Integer count
) {
}
