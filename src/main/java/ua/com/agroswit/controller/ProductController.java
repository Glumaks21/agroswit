package ua.com.agroswit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.dto.request.ProductCreationDTO;
import ua.com.agroswit.dto.response.ProductDTO;
import ua.com.agroswit.dto.response.converter.ProductDTOConverter;
import ua.com.agroswit.model.Product;
import ua.com.agroswit.service.ProductService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final ProductDTOConverter converter;

    @GetMapping
    ResponseEntity<List<ProductDTO>> getAll(@RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size,
                                            @RequestParam Optional<Integer> producerId) {
        var pageable = PageRequest.of(page - 1, size);

        var productPage = producerId.map(id -> service.getAllByProducer(id, pageable))
                .orElseGet(() -> service.getAll(pageable))
                .map(converter);

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

    @PostMapping
    ProductDTO create(@RequestBody @Validated ProductCreationDTO dto) {
        log.info("Received product creation request with dto: {}", dto);
        return converter.apply(service.create(dto));
    }

    @DeleteMapping(path = "/{id}")
    void delete(@PathVariable Long id) {
        log.info("Received product deleting request for id: {}", id);
        service.delete(id);
    }
}

