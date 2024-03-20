package ua.com.agroswit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.dto.ProductDTO;
import ua.com.agroswit.dto.converter.ProductDTOConverter;
import ua.com.agroswit.service.ProductService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final ProductDTOConverter converter;

    @GetMapping
    ResponseEntity<List<ProductDTO>> getAll(@RequestParam(required = false, defaultValue = "1") int page,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        var pageable = PageRequest.of(page - 1, size);
        var productPage = service.getAll(pageable).map(converter);

        var headers = new HttpHeaders() ;
        headers.add("X-Total-Count", String.valueOf(productPage.getTotalPages()));

        return new ResponseEntity<>(productPage.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        var product = service.getById(id).map(converter);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

