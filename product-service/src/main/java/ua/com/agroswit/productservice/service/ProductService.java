package ua.com.agroswit.productservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.productservice.dto.ProductCreationDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;

public interface ProductService {
    Page<ProductDTO> getAll(Pageable pageable);
    Page<ProductDTO> getAllActive(Pageable pageable);
    Page<ProductDTO> getAllByProducerId(Integer producerId, Pageable pageable);
    Page<ProductDTO> getAllActiveByProducerId(Integer producerId, Pageable pageable);
    ProductDTO getById(Integer id);

    ProductDTO create(ProductCreationDTO dto);

    ProductDTO update();
    void deactivate(Integer id);
}
