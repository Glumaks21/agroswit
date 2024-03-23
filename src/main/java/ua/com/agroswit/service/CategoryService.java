package ua.com.agroswit.service;

import ua.com.agroswit.dto.request.CategoryCreationDTO;
import ua.com.agroswit.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();
    Category create(CategoryCreationDTO dto);
}
