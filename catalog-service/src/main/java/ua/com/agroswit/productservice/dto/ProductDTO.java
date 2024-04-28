package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import ua.com.agroswit.productservice.dto.enums.ProductState;
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

        @Size(min = 2, max = 45, groups = Groups.Full.class, message = "Name must be between 2 and 45 symbols")
        @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Name must not be blank")
        String name,

        @Size(min = 10, max = 100, groups = Groups.Full.class,
                message = "Short description must be between 10 and 100 symbols")
        String shortDescription,

        @Size(min = 20, max = 1000, groups = Groups.Full.class,
                message = "Full description must be between 20 and 1000 symbols")
        String fullDescription,

        @Size(min = 20, max = 400, groups = Groups.Full.class,
                message = "Recommendations must be between 20 and 400 symbols")
        String recommendations,

        ProductType type,

        @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Producer ID is required")
        Integer producerId,

        @Positive(groups = Groups.Full.class, message = "Volume must be positive")
        @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Volume unit is required")
        Integer volume,

        @NotNull(groups = {Groups.Full.class, Groups.FullUpdate.class}, message = "Measurement unit is required")
        MeasurementUnitE unit,

        @JsonProperty(access = READ_ONLY)
        Boolean active,

        @JsonProperty(access = READ_ONLY)
        ProductState state,

        @Valid
        Set<ProductPropertyGroupDTO> propertyGroups,

        @Valid
        @NotEmpty(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Packages must not be empty")
        Set<PackageDTO> packages,

        @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Category ID is required")
        Integer categoryId,

        Set<Integer> filterIds) {
}
