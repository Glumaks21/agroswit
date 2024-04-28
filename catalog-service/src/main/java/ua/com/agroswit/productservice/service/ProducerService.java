package ua.com.agroswit.productservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;

import java.util.List;

public interface ProducerService {
    List<ProducerDTO> getAll();
    ProducerDTO getById(Integer id);
    ProducerDTO getByName(String name);
    Page<SimpleDetailedProductDTO> getAllByProductsById(Integer id, Pageable pageable);
    ProducerDTO create(ProducerDTO dto);
    String saveLogoById(Integer producerId, MultipartFile logo);
    ProducerDTO update(Integer id, ProducerDTO dto);
    void deleteById(Integer id);
    void removeLogoById(Integer id);
}
