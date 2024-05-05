package ua.com.agroswit.userdetails.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.userdetails.dto.response.CompanyDTO;
import ua.com.agroswit.userdetails.dto.request.CompanyModifyingDTO;

public interface CompanyService {
    Page<CompanyDTO> getAll(Pageable pageable);
    CompanyDTO getById(Integer id);
    CompanyDTO create(CompanyModifyingDTO dto);
    CompanyDTO update(Integer id, CompanyModifyingDTO dto);
    void deleteById(Integer id);
}
