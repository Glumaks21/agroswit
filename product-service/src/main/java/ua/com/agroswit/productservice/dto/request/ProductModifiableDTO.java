package ua.com.agroswit.productservice.dto.request;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import ua.com.agroswit.productservice.dto.validation.Groups;
import ua.com.agroswit.productservice.model.enums.MeasurementUnitE;
import ua.com.agroswit.productservice.model.enums.ProductType;

import java.util.Set;

@Data
public class ProductModifiableDTO {

    @Size(min = 2, max = 45, groups = {Groups.Create.class, Groups.FullUpdate.class},
            message = "Name must be between 2 and 45 symbols")
    @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class},
            message = "Name must not be blank")
    private String name;

    @Size(min = 20, max = 1000, groups = {Groups.Create.class, Groups.FullUpdate.class},
            message = "Full description must be between 20 and 1000 symbols")
    private String fullDescription;

    @Size(min = 10, max = 100, groups = {Groups.Create.class, Groups.FullUpdate.class},
            message = "Short description must be between 10 and 100 symbols")
    private String shortDescription;

    @Size(min = 20, max = 300, groups = {Groups.Create.class, Groups.FullUpdate.class},
            message = "Recommendations must be between 20 and 300 symbols")
    private String recommendations;

    private ProductType type;

    @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Volume unit is required")
    @Positive(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Volume must be positive")
    private Integer volume;

    @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Measurement unit is required")
    private MeasurementUnitE unit;

    @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Producer ID is required")
    private Integer producerId;

    @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Category ID is required")
    private Integer categoryId;

    @Valid
    private Set<PropertyGroupDTO> propertyGroups;

    @Valid
    @NotEmpty(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Packages must not be empty")
    private Set<PackageDTO> packages;

    private Set<Integer> filterIds;


    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            name = name.trim();
        }
        this.name = name;
    }

    public void setFullDescription(String fullDescription) {
        if (fullDescription != null && !fullDescription.isEmpty()) {
            fullDescription = fullDescription.trim();
        }
        this.fullDescription = fullDescription;
    }

    public void setShortDescription(String shortDescription) {
        if (shortDescription != null && !shortDescription.isEmpty()) {
            shortDescription = shortDescription.trim();
        }
        this.shortDescription = shortDescription;
    }

    public void setRecommendations(String recommendations) {
        if (recommendations != null && !recommendations.isEmpty()) {
            recommendations = recommendations.trim();
        }
        this.recommendations = recommendations;
    }

    @Data
    public static class PropertyGroupDTO {

        @Size(min = 2, max = 50, groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Property group name must be between 2 and 50")
        @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Property group name must not be blank")
        private String name;

        @Valid
        @Size(min = 1, message = "Property group must have at least one property")
        @NotNull(message = "Properties in group are required")
        private Set<PropertyDTO> properties;

        public void setName(String name) {
            if (name != null && !name.isEmpty()) {
                name = name.trim();
            }
            this.name = name;
        }
    }

    @JsonView({Views.Create.class, Views.Update.class})
    @Data
    public static class PropertyDTO {

        @Size(min = 2, max = 50, groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Property name must be between 2 and 50")
        @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Property name must not be blank")
        private String name;

        @Size(min = 2, max = 255, groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Property name must be between 2 and 255")
        @NotBlank(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Property value must not be blank")
        private String value;

        public void setName(String name) {
            if (name != null && !name.isEmpty()) {
                name = name.trim();
            }
            this.name = name;
        }
    }

    @JsonView({Views.Create.class, Views.Update.class})
    @Data
    public static class PackageDTO {

        @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Package price is required")
        @Positive(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Package price must be positive")
        private Double price;

        @NotNull(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Package count is required")
        @Positive(groups = {Groups.Create.class, Groups.FullUpdate.class}, message = "Package count must be positive")
        private Integer count;
    }
}
