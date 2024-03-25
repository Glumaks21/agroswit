package ua.com.agroswit.service;

import ua.com.agroswit.dto.request.CategoryCreationDTO;
import ua.com.agroswit.dto.response.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO getById(Integer id);
    CategoryDTO create(CategoryCreationDTO dto);
    void deleteById(Integer id);
}
