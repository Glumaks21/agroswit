package ua.com.agroswit.productservice.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Set;

public record PesticideDTO(
        @JsonUnwrapped
        DetailedProductDTO product,
        Set<String> pests,
        Set<String> cultures) {
}
