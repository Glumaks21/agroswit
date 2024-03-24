package ua.com.agroswit.service;

import ua.com.agroswit.dto.request.CategoryCreationDTO;
import ua.com.agroswit.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    List<Category> getAllSubcategories(Integer parentId);
    Category createCategory(CategoryCreationDTO dto);
    Category createSubcategory(CategoryCreationDTO dto, Integer parentId);
}
