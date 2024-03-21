package ua.com.agroswit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.dto.response.CategoryDTO;
import ua.com.agroswit.dto.response.converter.CategoryDTOConverter;
import ua.com.agroswit.service.CategoryService;

import java.util.Collection;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;
    private final CategoryDTOConverter converter;

    @GetMapping
    Collection<CategoryDTO> getAll() {
        return service.getAll().stream()
                .map(converter)
                .toList();
    }
}
