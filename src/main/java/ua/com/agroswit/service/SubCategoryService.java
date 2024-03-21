package ua.com.agroswit.service;

import ua.com.agroswit.model.SubCategory;

import java.util.List;

public interface SubCategoryService {
    List<SubCategory> getAllByCategoryId(Integer categoryId);
}
