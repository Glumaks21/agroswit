package ua.com.agroswit.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.productservice.dto.SimplifiedProductDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.SearchParams;
import ua.com.agroswit.productservice.service.ProductService;
import ua.com.agroswit.productservice.dto.Groups;

import java.util.Collection;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Product", description = "Product management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Retrieve all products")
//    @ApiResponse(headers = @Header(name = "X-Total-Count", description = "Count of available pages"))
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Page<ProductDTO> getAll(@PageableDefault Pageable pageable,
                            SearchParams searchParams) {
        return productService.getAll(pageable, searchParams);
    }

    @Operation(summary = "Retrieve producers by 1C ID")
    @GetMapping(produces = APPLICATION_JSON_VALUE, params = "1c_id")
    Page<ProductDTO> getBy1CIds(@PageableDefault Pageable pageable, @RequestParam(name = "1c_id") List<Integer> ids) {
        return productService.getAllBy1CIds(ids, pageable);
    }

    @Operation(summary = "Retrieve product by id")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ProductDTO getById(@PathVariable Integer id) {
        return productService.getById(id);
    }

    @Operation(summary = "Create product")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ProductDTO create(@RequestBody @Validated(Groups.Create.class) SimplifiedProductDTO dto) {
        log.info("Received product creation request with dto: {}", dto);
        return productService.create(dto);
    }

    @Operation(summary = "Remove product by id")
    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    void remove(@PathVariable Integer id) {
        log.info("Received product deleting request for id: {}", id);
        productService.deactivate(id);
    }
}
