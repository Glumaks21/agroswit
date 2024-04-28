package ua.com.agroswit.productservice.dto.response;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ua.com.agroswit.productservice.dto.ProductDTO;


//@JsonInclude(JsonInclude.Include.NON_NULL)
public record DetailedProductDTO(
        @JsonUnwrapped
        ProductDTO product,
        Integer article1CId,
        Boolean inStock
) {
}
