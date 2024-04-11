package ua.com.agroswit.productservice.service;

import ua.com.agroswit.productservice.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllHighLevelCategories();

    CategoryDTO getById(Integer id);

    CategoryDTO create(CategoryDTO dto);

    CategoryDTO updateById(Integer id, CategoryDTO dto);

    void deleteById(Integer id);
}
