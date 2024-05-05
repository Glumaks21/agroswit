package ua.com.agroswit.productservice.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.enums.ProductState;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;


//@JsonInclude(JsonInclude.Include.NON_NULL)
public record DetailedProductDTO(
        @JsonUnwrapped
        ProductDTO product,
        ProductState state,
        Integer article1CId,
        Boolean inStock
) {
}
