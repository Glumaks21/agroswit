package ua.com.agroswit.productservice.service;

import ua.com.agroswit.productservice.dto.FilterDTO;

import java.util.Collection;
import java.util.List;

public interface FilterService {
    List<FilterDTO> getAllHighLevel();
    List<FilterDTO> getAllByIds(Collection<Integer> ids);
    FilterDTO getById(Integer id);
    FilterDTO create(FilterDTO filter);
    FilterDTO update(Integer id, FilterDTO dto);
    void deleteById(Integer id);
}
