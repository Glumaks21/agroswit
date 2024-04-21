package ua.com.agroswit.productservice.service;

import ua.com.agroswit.productservice.dto.CultureDTO;

import java.util.List;

public interface CultureService {
    List<CultureDTO> getAll();
    CultureDTO getByName(String name);
    CultureDTO create(CultureDTO dto);
    void deleteById(Integer id);
}
