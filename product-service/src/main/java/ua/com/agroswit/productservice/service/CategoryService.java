package ua.com.agroswit.productservice.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.SimplifiedCategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<SimplifiedCategoryDTO> getAll();
    List<SimplifiedCategoryDTO> getAllLowLevel();
    CategoryDTO getById(Integer id);
    CategoryDTO getByName(String name);
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO saveLogo(Integer categoryId, MultipartFile logo);
    CategoryDTO updateById(Integer id, CategoryDTO dto);
    void deleteById(Integer id, Integer replaceId);
    void deleteLogoById(Integer categoryId);
}
