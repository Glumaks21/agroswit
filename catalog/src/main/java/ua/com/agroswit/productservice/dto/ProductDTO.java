package ua.com.agroswit.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ua.com.agroswit.productservice.dto.enums.ProductState;
import ua.com.agroswit.productservice.dto.validation.Groups.Create;
import ua.com.agroswit.productservice.dto.validation.Groups.FullUpdate;
import ua.com.agroswit.productservice.dto.validation.Groups.PartialUpdate;
import ua.com.agroswit.productservice.model.enums.MeasurementUnitE;
import ua.com.agroswit.productservice.model.enums.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public record ProductDTO(

        @JsonProperty(access = READ_ONLY)
        Integer id,

        @JsonProperty(access = READ_ONLY)
        String imageUrl,

        @Size(min = 2, max = 45, groups = {Create.class, FullUpdate.class, PartialUpdate.class}, message = "Name must be between 2 and 45 symbols")
        @NotBlank(groups = {Create.class, FullUpdate.class}, message = "Name must not be blank")
        String name,

        @Size(min = 10, max = 100, groups = {Create.class, FullUpdate.class, PartialUpdate.class},
                message = "Short description must be between 10 and 100 symbols")
        String shortDescription,

        @Size(min = 20, max = 1000, groups = {Create.class, FullUpdate.class, PartialUpdate.class},
                message = "Full description must be between 20 and 1000 symbols")
        String fullDescription,

        @Size(min = 20, max = 400, groups = {Create.class, FullUpdate.class, PartialUpdate.class},
                message = "Recommendations must be between 20 and 400 symbols")
        String recommendations,

        ProductType type,

        @NotNull(groups = {Create.class, FullUpdate.class, PartialUpdate.class}, message = "Producer ID is required")
        Integer producerId,

        @Positive(groups = {Create.class, FullUpdate.class, PartialUpdate.class}, message = "Volume must be positive")
        @NotNull(groups = {Create.class, FullUpdate.class}, message = "Volume unit is required")
        Integer volume,

        @NotNull(groups = {Create.class, FullUpdate.class, PartialUpdate.class}, message = "Measurement unit is required")
        MeasurementUnitE unit,

        @JsonProperty(access = READ_ONLY)
        Boolean active,

        @Valid
        Set<ProductPropertyGroupDTO> propertyGroups,

        @Valid
        @Size(min = 1, groups = {Create.class, FullUpdate.class, PartialUpdate.class},
                message = "Packages must have at least one element")
        @NotNull(groups = {Create.class, FullUpdate.class}, message = "Packages must not be empty")
        Set<PackageDTO> packages,

        @NotNull(groups = {Create.class, FullUpdate.class}, message = "Category ID is required")
        Integer categoryId,

        Set<Integer> filterIds,

        @JsonProperty(access = READ_ONLY)
        LocalDateTime createdAt,

        @JsonProperty(access = READ_ONLY)
        LocalDateTime modifiedAt
) {
}
