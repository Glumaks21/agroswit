package ua.com.agroswit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.dto.request.ProductCreationDTO;
import ua.com.agroswit.dto.response.ProductDTO;
import ua.com.agroswit.model.Product;
import ua.com.agroswit.service.ProductService;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Product", description = "Product management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Operation(summary = "Retrieve all products")
    @ApiResponse(headers = @Header(name = "X-Total-Count", description = "Count of available pages"))
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<ProductDTO>> getAll(@RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size,
                                            @RequestParam(required = false) Optional<Integer> producerId) {
        var pageable = PageRequest.of(page - 1, size);

        var productPage = producerId.map(id -> service.getAllByProducer(id, pageable))
                .orElseGet(() -> service.getAll(pageable));

        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(productPage.getTotalPages()));

        return new ResponseEntity<>(productPage.getContent(), headers, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve product by id")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        var product = service.getById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retrieve product by id")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Product create(@RequestBody @Validated ProductCreationDTO dto) {
        log.info("Received product creation request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Remove product by id")
    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    void delete(@PathVariable Long id) {
        log.info("Received product deleting request for id: {}", id);
        service.delete(id);
    }
}

