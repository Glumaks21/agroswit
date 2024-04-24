package ua.com.agroswit.productservice.dto.request;


import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@ParameterObject
public record ProductSearchParams(
        @Parameter(name = "producer_id", description = "Filter to retrieve products of specified producer")
        Integer producer_id,

        @Parameter(name = "active", description = "Filter to retrieve active products")
        Boolean active,

        @Parameter(name = "f_id", description = "Ids of filters")
        List<Integer> f_id
) {
}
