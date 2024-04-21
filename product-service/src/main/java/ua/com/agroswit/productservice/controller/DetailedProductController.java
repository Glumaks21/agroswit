package ua.com.agroswit.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.productservice.dto.request.ProductSearchParams;
import ua.com.agroswit.productservice.dto.response.DetailedProductDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;
import ua.com.agroswit.productservice.service.ProductService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Product detailed management API",
        description = "Include inventory details")
@Slf4j
@RestController
@RequestMapping("/api/v1/products/detailed")
@RequiredArgsConstructor
public class DetailedProductController {

    private final ProductService productService;


    @Operation(summary = "Retrieve all products")
    @ApiResponse(responseCode = "200",
            headers = @Header(
                    name = "X-Total-Count",
                    description = "Count of available elements"))
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<SimpleDetailedProductDTO>> getAll(@ParameterObject @PageableDefault Pageable pageable,
                                                          ProductSearchParams searchParams) {
        var productPage = productService.getAllDetailed(pageable, searchParams);

        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(productPage.getTotalElements()));

        return new ResponseEntity<>(productPage.getContent(), headers, OK);
    }

    @Operation(summary = "Retrieve product by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    DetailedProductDTO getById(@PathVariable Integer id) {
        return productService.getDetailedById(id);
    }
}