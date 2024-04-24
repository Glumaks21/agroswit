package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import ua.com.agroswit.productservice.dto.validation.Groups;
import ua.com.agroswit.productservice.model.enums.MeasurementUnitE;
import ua.com.agroswit.productservice.model.enums.ProductType;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public record ProductDTO(

        @JsonProperty(access = READ_ONLY)
        Integer id,

        @JsonProperty(access = READ_ONLY)
        String imageUrl,

        @Size(min = 2, max = 45, groups = {Groups.Create.class, Groups.FullUpdate.class},
                message = "Name must be between 2 and 45 symbols")
        @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must not be blank")
        String name,

        @Size(min = 10, max = 100, groups = {Groups.Create.class, Groups.FullUpdate.class},
                message = "Short description must be between 10 and 100 symbols")
        String shortDescription,

        @Size(min = 20, max = 1000, groups = {Groups.Create.class, Groups.FullUpdate.class},
                message = "Full description must be between 20 and 1000 symbols")
        String fullDescription,

        @Size(min = 20, max = 400, groups = {Groups.Create.class, Groups.FullUpdate.class},
                message = "Recommendations must be between 20 and 400 symbols")
        String recommendations,

        ProductType type,

        @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Producer ID is required")
        Integer producerId,

        @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Volume unit is required")
        @Positive(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Volume must be positive")
        Integer volume,

        @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Measurement unit is required")
        MeasurementUnitE unit,

        @JsonProperty(access = READ_ONLY)
        Boolean active,

        @Valid
        Set<PropertyGroupDTO> propertyGroups,

        @Valid
        @NotEmpty(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Packages must not be empty")
        Set<PackageDTO> packages,

        @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Category ID is required")
        Integer categoryId,

        Set<Integer> filterIds) {

    public record PropertyGroupDTO(

            @Size(min = 2, max = 50, groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must be between 2 and 50")
            @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must not be blank")
            String name,

            @Valid
            @Size(min = 1, message = "Property group must have at least one property")
            @NotNull(message = "Properties in group are required")
            Set<PropertyDTO> properties) {
    }

    public record PropertyDTO(

            @Size(min = 2, max = 50, groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must be between 2 and 50")
            @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must not be blank")
            String name,

            @Size(max = 255, groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Value must be lower then 255 symbols")
            @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Value must not be blank")
            String value
    ) {
    }

    public record PackageDTO(

            @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Price is required")
            @Positive(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Price must be positive")
            Double price,

            @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Count is required")
            @Positive(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Count must be positive")
            Integer count) {
    }
}
