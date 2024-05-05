package ua.com.agroswit.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;
import ua.com.agroswit.productservice.dto.validation.Groups;
import ua.com.agroswit.productservice.dto.response.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.service.CategoryService;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Category", description = "Category management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;


    @Operation(summary = "Retrieve all categories", parameters = {
            @Parameter(name = "lowest", description = "If specified, then response is array of categories that don't have subcategories"),
            @Parameter(name = "name", description = "If specified, then response is object")
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Collection<SimplifiedCategoryDTO> getAll(@RequestParam Optional<String> lowest) {
        if (lowest.isPresent()) {
            return service.getAllLowLevel();
        }
        return service.getAll();
    }

    @Operation(summary = "Retrieve category by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    CategoryDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Retrieve category by name", hidden = true)
    @GetMapping(params = "name", produces = APPLICATION_JSON_VALUE)
    CategoryDTO getByName(@RequestParam String name) {
        return service.getByName(name);
    }

    @Operation(summary = "Retrieve all producers of products in category")
    @GetMapping(path = "/{id}/producers", produces = APPLICATION_JSON_VALUE)
    Collection<ProducerDTO> getAllProducers(@PathVariable Integer id) {
        return service.getAllProducersById(id);
    }

    @Operation(summary = "Retrieve all products by category ID")
    @GetMapping(path = "/{id}/products", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<SimpleDetailedProductDTO>> getAllProducts(
            @PathVariable Integer id,
            @ParameterObject @PageableDefault Pageable pageable) {
        var page = service.getAllProductsById(id, pageable);

        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));

        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    @Operation(summary = "Create new category")
    @ResponseStatus(CREATED)
    @PostMapping(path = {"", "/{parentCategoryId}"}, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO createCategory(
            @PathVariable Optional<Integer> parentCategoryId,
            @Validated(Groups.Create.class)
            @RequestBody CategoryDTO dto) {
        //TODO change dto
//        parentCategoryId.ifPresent(dto::setParentCategoryId);
        log.info("Received category creating request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Save category logo")
    @ResponseStatus(CREATED)
    @PostMapping(path = "/{categoryId}/logo", consumes = MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> saveLogo(@PathVariable Integer categoryId, MultipartFile logo) {
        log.info("Received logo saving request with file: {}", logo.getOriginalFilename());
        var logoUrl = service.saveLogo(categoryId, logo);
        return ResponseEntity.created(URI.create(logoUrl)).build();
    }

    @Operation(summary = "Full updating category by ID")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO update(@PathVariable Integer id,
                       @Validated(Groups.FullUpdate.class)
                       @RequestBody CategoryDTO dto) {
        log.info("Received updating request for category with id {} and dto {}", id, dto);
        return service.update(id, dto);
    }

    @Operation(summary = "Delete category by ID", parameters = @Parameter(
            name = "replaceId", description = "Id of category to replace products relation of deleted category")
    )
    @DeleteMapping(path = "/{id}")
    void delete(@PathVariable Integer id, @RequestParam Optional<Integer> replaceId) {
        if (replaceId.isPresent()) {
            log.info("Received logo deleting request for category with id {} and replacement to {}", id, replaceId.get());
            service.deleteById(id, replaceId.get());
            return;
        }
        log.info("Received deleting request for category with id {}", id);
        service.deleteById(id, null);
    }

    @Operation(summary = "Delete category logo by ID")
    @DeleteMapping(path = "/{categoryId}/logo")
    void deleteLogo(@PathVariable Integer categoryId) {
        log.info("Received logo deleting request for category with id {}", categoryId);
        service.deleteLogoById(categoryId);
    }
}
