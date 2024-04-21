package ua.com.agroswit.productservice.dto.request;


import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public record ProductSearchParams(
        @Parameter(name = "producerId", description = "Filter to retrieve products of specified producer")
        Integer producerId,

        @Parameter(name = "active", description = "Filter to retrieve active products")
        String active
) {
}
