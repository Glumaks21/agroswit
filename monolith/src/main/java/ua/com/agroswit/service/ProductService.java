package ua.com.agroswit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.dto.request.ProductCreationDTO;
import ua.com.agroswit.dto.ProductDTO;

import java.util.Optional;

public interface ProductService {
    Page<ProductDTO> getAll(Pageable pageable);
    Page<ProductDTO> getAllByProducer(Integer producerId, Pageable pageable);
    Optional<ProductDTO> getById(Integer id);
    Optional<ProductDTO> getByName(String name);
    ProductDTO create(ProductCreationDTO dto);
    ProductDTO update();
    void delete(Integer id);
}
