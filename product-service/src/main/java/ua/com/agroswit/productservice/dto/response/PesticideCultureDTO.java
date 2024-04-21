package ua.com.agroswit.productservice.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PesticideCultureDTO(

        Integer id,

        @Size(min = 2, max = 50, message = "Culture name must be between 2 and 50")
        @NotBlank(message = "Culture name is required")
        String name,

        @Positive(message = "Culture min volume must be positive")
        @NotNull(message = "Culture min volume is required")
        Double minVolume,

        @Positive(message = "Culture max volume must be positive")
        @NotNull(message = "Culture max volume is required")
        Double maxVolume

) {
}
