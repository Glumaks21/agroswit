package ua.com.agroswit.productservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.request.CategoryModifiableDTO;
import ua.com.agroswit.productservice.dto.response.CategoryDTO;
import ua.com.agroswit.productservice.dto.response.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;

import java.util.List;

public interface CategoryService {
    List<SimplifiedCategoryDTO> getAll();
    List<SimplifiedCategoryDTO> getAllLowLevel();
    CategoryDTO getById(Integer id);
    CategoryDTO getByName(String name);
    Page<SimpleDetailedProductDTO> getAllProductsById(Integer id, Pageable pageable);
    CategoryDTO create(CategoryModifiableDTO dto);
    CategoryDTO saveLogo(Integer categoryId, MultipartFile logo);
    CategoryDTO update(Integer id, CategoryModifiableDTO dto);
    void deleteById(Integer id, Integer replaceId);
    void deleteLogoById(Integer categoryId);
}
