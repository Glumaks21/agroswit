package ua.com.agroswit.productservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.productservice.dto.request.PesticideModifiableDTO;
import ua.com.agroswit.productservice.dto.response.PesticideDTO;

public interface PesticideService {
    Page<PesticideDTO> getAll(Pageable pageable);
    PesticideDTO getById(Integer id);
    PesticideDTO create(PesticideModifiableDTO dto);
}
