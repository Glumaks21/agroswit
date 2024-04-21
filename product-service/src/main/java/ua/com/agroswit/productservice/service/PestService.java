package ua.com.agroswit.productservice.service;

import ua.com.agroswit.productservice.dto.PestDTO;

import java.util.List;

public interface PestService {
    List<PestDTO> getAll();
    PestDTO create(PestDTO dto);
    void deleteById(Integer id);
}
