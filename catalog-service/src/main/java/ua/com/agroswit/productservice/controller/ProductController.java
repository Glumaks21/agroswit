package ua.com.agroswit.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.request.ProductSearchParams;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.validation.Groups;
import ua.com.agroswit.productservice.service.ProductService;

import java.net.URI;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;


    @Operation(summary = "Retrieve all",
            parameters = @Parameter(name = "id", description = "Id of requested products"),
            responses = @ApiResponse(
                    responseCode = "200",
                    headers = @Header(
                    name = "X-Total-Count", description = "Total count of elements")))
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<ProductDTO>> getAll(@ParameterObject @PageableDefault Pageable pageable,
                                            @ParameterObject ProductSearchParams searchParams) {
        var productPage = service.getAll(pageable, searchParams);

        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(productPage.getTotalElements()));

        return new ResponseEntity<>(productPage.getContent(), headers, OK);
    }

    @Operation(summary = "Retrieve by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ProductDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping(params = "id", produces = APPLICATION_JSON_VALUE)
    List<ProductDTO> getByIds(@RequestParam(name = "id") List<Integer> ids) {
        return service.getAllByIds(ids);
    }

    @Operation(summary = "Create product")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ProductDTO create(@RequestBody @Validated(Groups.Create.class) ProductDTO dto) {
        log.info("Received product creation request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Save product image")
    @ResponseStatus(CREATED)
    @PostMapping(path = "/{id}/image", consumes = MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> saveImage(@PathVariable Integer id, MultipartFile image) {
        log.info("Received product's image saving request for id {} with file: {}",
                id, image.getOriginalFilename());
        var imageUrl = service.saveImageById(id, image);
        return ResponseEntity.created(URI.create(imageUrl)).build();
    }

    @Operation(summary = "Activate product by IDs")
    @PostMapping(path = "/activate", params = "id")
    void activate(@RequestParam(name = "id") Set<Integer> ids) {
        log.info("Received activation request for ids: {}", ids);
        service.activateByIds(ids);
    }

    @Operation(summary = "Full update product by ID")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ProductDTO fullUpdate(@PathVariable Integer id,
                          @RequestBody @Validated(Groups.FullUpdate.class) ProductDTO dto) {
        log.info("Received product full updating request with dto: {}", dto);
        return service.fullUpdateById(id, dto);
    }

    @Operation(summary = "Partial update product by ID")
    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ProductDTO partialUpdate(@PathVariable Integer id,
                          @RequestBody @Validated(Groups.PartialUpdate.class) ProductDTO dto) {
        log.info("Received product partial updating request with dto: {}", dto);
        return service.partialUpdateById(id, dto);
    }

    @Operation(summary = "Deactivate product by ID")
    @DeleteMapping(path = "/{id}")
    void remove(@PathVariable Integer id) {
        log.info("Received product deactivating request for id: {}", id);
        service.deactivateById(id);
    }

    @Operation(summary = "Remove product image")
    @DeleteMapping(path = "/{id}/image")
    void removeImage(@PathVariable Integer id) {
        log.info("Received product's image removing request for id: {}", id);
        service.removeImageById(id);
    }

}

