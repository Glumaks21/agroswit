package ua.com.agroswit.productservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.productservice.dto.SimplifiedProductDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.SearchParams;

import java.util.Collection;

public interface ProductService {
    Page<ProductDTO> getAll(Pageable pageable);
    Page<ProductDTO> getAll(Pageable pageable, SearchParams searchParams);
    Page<ProductDTO> getAllBy1CIds(Collection<Integer> ids, Pageable pageable);
    ProductDTO getById(Integer id);
    ProductDTO getBy1CId(Integer article1CId);
    ProductDTO create(SimplifiedProductDTO dto);
    ProductDTO update();
    void deactivate(Integer id);
}
