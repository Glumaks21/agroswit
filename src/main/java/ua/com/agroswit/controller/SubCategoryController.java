package ua.com.agroswit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.dto.request.SubCategoryCreationDTO;
import ua.com.agroswit.dto.response.SubCategoryDTO;
import ua.com.agroswit.dto.response.converter.SubCategoryDTOConverter;
import ua.com.agroswit.model.SubCategory;
import ua.com.agroswit.service.SubCategoryService;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Tag(name = "Subcategory", description = "Subcategory management API")
@RestController
@RequestMapping("/api/v1/categories/{categoryId}/subcategories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService service;
    private final SubCategoryDTOConverter converter;

    @Operation(summary = "Retrieve all subcategories")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Collection<SubCategoryDTO> getAll(@PathVariable Integer categoryId) {
        return service.getAllByCategoryId(categoryId).stream()
                .map(converter)
                .toList();
    }

    @Operation(summary = "Create new subcategory")
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    SubCategoryDTO create(
            @PathVariable Integer categoryId,
            @RequestBody @Valid SubCategoryCreationDTO dto) {
        return converter.apply(service.create(categoryId, dto));
    }
}
