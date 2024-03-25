package ua.com.agroswit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.dto.request.CategoryCreationDTO;
import ua.com.agroswit.dto.response.CategoryDTO;
import ua.com.agroswit.dto.response.SimplifiedCategoryDTO;
import ua.com.agroswit.service.CategoryService;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Category", description = "Category management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @Operation(summary = "Retrieve all categories")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Collection<CategoryDTO> getAllCategories() {
        return service.getAllCategories();
    }

    @Operation(summary = "Retrieve category by ID")
    @GetMapping(path = "/{categoryId}", produces = APPLICATION_JSON_VALUE)
    CategoryDTO getById(@PathVariable Integer categoryId) {
        return service.getById(categoryId);
    }

    @Operation(summary = "Create new category")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO createCategory(@RequestBody @Valid CategoryCreationDTO dto) {
        log.info("Received category creating request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Create new subcategory")
    @PostMapping(path = "/{categoryId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO createSubcategory(@PathVariable Integer categoryId,
                                            @RequestBody @Valid CategoryCreationDTO dto) {
        var dtoWithParentCategoryId = new CategoryCreationDTO(
                dto.name(), dto.description(), categoryId, dto.properties());
        log.info("Received subcategory creating request with dto: {}", dto);
        return service.create(dtoWithParentCategoryId);
    }

    @Operation(summary = "Delete category by id")
    @DeleteMapping(path = "/{categoryId}", consumes = APPLICATION_JSON_VALUE)
    void deleteCategory(@PathVariable Integer categoryId) {
        log.info("Received deleting request for category with id {}", categoryId);
        service.deleteById(categoryId);
    }
}
