package ua.com.agroswit.productservice.dto;


import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public record SearchParams(
        @Parameter(name = "Active", description = "Filter to retrieve products of specified producer")
        Integer producerId,

        @Parameter(name = "active", description = "Filter to retrieve active products")
        String active
) {
}
