package ua.com.agroswit.service;

import ua.com.agroswit.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO getById(Integer id);
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO updateById(Integer id, CategoryDTO dto);
    void deleteById(Integer id);
}
