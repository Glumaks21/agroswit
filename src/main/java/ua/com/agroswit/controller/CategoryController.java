package ua.com.agroswit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.dto.request.CategoryCreationDTO;
import ua.com.agroswit.dto.response.CategoryDTO;
import ua.com.agroswit.dto.response.converter.CategoryDTOConverter;
import ua.com.agroswit.service.CategoryService;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Category", description = "Category management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;
    private final CategoryDTOConverter converter;

    @Operation(summary = "Retrieve all categories",
            description = "If category id specified in url, then retrieves subcategory")
    @GetMapping(path = {"", "/{categoryId}"}, produces = APPLICATION_JSON_VALUE)
    Collection<CategoryDTO> getAll(@PathVariable(required = false) Optional<Integer> categoryId) {
        var categories = categoryId.map(service::getAllSubcategories)
                .orElseGet(service::getAllCategories);

        return categories.stream()
                .map(converter)
                .toList();
    }

    @Operation(summary = "Create new category",
            description = "If category id specified in url then creates subcategory")
    @PostMapping(path = {"", "/{categoryId}"}, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO create(@PathVariable(required = false) Optional<Integer> categoryId,
            @RequestBody @Valid CategoryCreationDTO dto) {

        var category = categoryId.map(id -> service.createSubcategory(dto, id))
                .orElseGet(() -> service.createCategory(dto));

        return converter.apply(category);
    }
}
