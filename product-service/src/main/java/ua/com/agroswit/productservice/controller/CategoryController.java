package ua.com.agroswit.productservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.Groups;
import ua.com.agroswit.productservice.dto.Views;
import ua.com.agroswit.productservice.service.CategoryService;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
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
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    CategoryDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Create new category")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO createCategory(@JsonView(Views.Create.class)
                               @RequestBody CategoryDTO dto) {
        log.info("Received category creating request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Create new category with parent ID specified in URL")
    @ResponseStatus(CREATED)
    @PostMapping(path = "/{categoryId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO createSubcategory(@PathVariable Integer categoryId,
                                  @Valid
                                  @JsonView(Views.Create.class)
                                  @RequestBody CategoryDTO dto) {
        var dtoWithParentCategoryId = CategoryDTO.builder()
                .name(dto.name())
                .description(dto.description())
                .parentCategoryId(categoryId)
                .properties(dto.properties())
                .build();
        log.info("Received subcategory creating request with dto: {}", dto);
        return service.create(dtoWithParentCategoryId);
    }

    @Operation(summary = "Partial updating category by ID")
    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO update(@PathVariable Integer id,
                      @Valid @JsonView(Views.Patch.class) @RequestBody CategoryDTO dto) {
        log.info("Received updating request for category with id {} and dto {}", id, dto);
        return service.updateById(id, dto);
    }

    @Operation(summary = "Delete category by ID")
    @DeleteMapping(path = "/{categoryId}", consumes = APPLICATION_JSON_VALUE)
    void deleteCategory(@PathVariable Integer categoryId) {
        log.info("Received deleting request for category with id {}", categoryId);
        service.deleteById(categoryId);
    }
}
