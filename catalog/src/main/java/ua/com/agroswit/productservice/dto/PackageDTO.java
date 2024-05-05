package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ua.com.agroswit.productservice.dto.request.Views;
import ua.com.agroswit.productservice.dto.request.Views.FullUpdateView;
import ua.com.agroswit.productservice.dto.request.Views.FullView;
import ua.com.agroswit.productservice.dto.request.Views.PartialUpdateView;
import ua.com.agroswit.productservice.dto.validation.Groups;
import ua.com.agroswit.productservice.dto.validation.Groups.Create;
import ua.com.agroswit.productservice.dto.validation.Groups.FullUpdate;
import ua.com.agroswit.productservice.dto.validation.Groups.PartialUpdate;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@JsonView(FullView.class)
public record PackageDTO(

        @JsonView({FullUpdateView.class, PartialUpdateView.class})
        @NotNull(groups = {FullUpdate.class, PartialUpdate.class}, message = "Id is required")
        Integer id,

        @Positive(groups = {Create.class, FullUpdate.class, PartialUpdate.class}, message = "Price must be positive")
        @NotNull(groups = {Create.class, FullUpdate.class, PartialUpdate.class}, message = "Price is required")
        Double price,

        @JsonProperty(access = READ_ONLY)
        Double oldPrice,

        @NotNull(groups = {Create.class, FullUpdate.class, PartialUpdate.class}, message = "Count is required")
        @Positive(groups = {Create.class, FullUpdate.class, PartialUpdate.class}, message = "Count must be positive")
        Integer count
) {
}
