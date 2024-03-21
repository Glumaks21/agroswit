package ua.com.agroswit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.dto.response.SubCategoryDTO;
import ua.com.agroswit.dto.response.converter.SubCategoryDTOConverter;
import ua.com.agroswit.service.SubCategoryService;

import java.util.Collection;


@RestController
@RequestMapping("/api/v1/categories/{categoryId}/subcategories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService service;
    private final SubCategoryDTOConverter converter;

    @GetMapping
    Collection<SubCategoryDTO> getAll(@PathVariable Integer categoryId) {
        return service.getAllByCategoryId(categoryId).stream()
                .map(converter)
                .toList();
    }

}
