package ua.com.agroswit.productservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.Groups;
import ua.com.agroswit.productservice.dto.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.dto.Views;
import ua.com.agroswit.productservice.service.CategoryService;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Category", description = "Category management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;


    @JsonView(Views.Detailed.class)
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
    CategoryDTO getBName(@RequestParam String name) {
        return service.getByName(name);
    }

    @Operation(summary = "Create new category")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO createCategory(
            @Validated(Groups.Create.class)
            @JsonView(Views.Create.class)
            @RequestBody CategoryDTO dto) {
        log.info("Received category creating request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Create new category with parent ID specified in URL")
    @ResponseStatus(CREATED)
    @PostMapping(path = "/{categoryId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO createSubcategory(@PathVariable Integer categoryId,
                                  @Validated(Groups.Create.class)
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

    @Operation(summary = "Save category logo")
    @PostMapping(path = "/{categoryId}/logo", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO saveLogo(@PathVariable Integer categoryId, MultipartFile logo) {
        log.info("Received logo saving request with file: {}", logo.getOriginalFilename());
        return service.saveLogo(categoryId, logo);
    }

    @Operation(summary = "Full updating category by ID")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CategoryDTO update(@PathVariable Integer id,
                       @Validated(Groups.Update.class)
                       @JsonView(Views.Update.class)
                       @RequestBody CategoryDTO dto) {
        log.info("Received updating request for category with id {} and dto {}", id, dto);
        return service.updateById(id, dto);
    }

    @Operation(summary = "Delete category by ID", parameters = @Parameter(
            name = "replaceId", description = "Id of category to replace products relation of deleting category")
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
