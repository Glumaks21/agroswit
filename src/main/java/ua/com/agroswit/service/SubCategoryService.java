package ua.com.agroswit.service;

import ua.com.agroswit.dto.request.SubCategoryCreationDTO;
import ua.com.agroswit.model.SubCategory;

import java.util.List;

public interface SubCategoryService {
    List<SubCategory> getAllByCategoryId(Integer categoryId);
    SubCategory create(Integer categoryId, SubCategoryCreationDTO dto);
}
