package ua.com.agroswit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductCreationDTO(
        @NotBlank
        String name,
        @Positive
        @NotNull
        Double price,
        @Positive
        @NotNull
        Integer producerId) {
}
