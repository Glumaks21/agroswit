package ua.com.agroswit.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.productservice.dto.ProductCreationDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.service.ProductService;

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

    @Operation(summary = "Retrieve all products",
            description = "If active param specified return only active products")
//    @ApiResponse(headers = @Header(name = "X-Total-Count", description = "Count of available pages"))
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<ProductDTO>> getAll(@PageableDefault(page = 1) Pageable pageable,
                                            @RequestParam(required = false) Optional<?> active,
                                            @RequestParam(required = false) Optional<Integer> producerId) {
        if (pageable.getPageNumber() >= 0) {
            pageable = pageable.withPage(pageable.getPageNumber() - 1);
        }

        Page<ProductDTO> productPage;
        if (active.isPresent() && producerId.isPresent()) {
            productPage = service.getAllActiveByProducerId(producerId.get(), pageable);
        } else if (producerId.isPresent()) {
            productPage = service.getAllByProducerId(producerId.get(), pageable);
        } else if (active.isPresent()) {
            productPage = service.getAllActive(pageable);
        } else {
            productPage = service.getAll(pageable);
        }

        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(productPage.getTotalPages()));

        return new ResponseEntity<>(productPage.getContent(), headers, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve product by id")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ProductDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Create product")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ProductDTO create(@RequestBody @Validated ProductCreationDTO dto) {
        log.info("Received product creation request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Remove product by id")
    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    void remove(@PathVariable Integer id) {
        log.info("Received product deleting request for id: {}", id);
        service.deactivate(id);
    }
}
