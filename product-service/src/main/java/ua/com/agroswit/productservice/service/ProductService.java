package ua.com.agroswit.productservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.request.ProductModifiableDTO;
import ua.com.agroswit.productservice.dto.response.DetailedProductDTO;
import ua.com.agroswit.productservice.dto.request.ProductSearchParams;
import ua.com.agroswit.productservice.dto.response.ProductDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;

import java.util.Collection;
import java.util.List;

public interface ProductService {
    Page<ProductDTO> getAll(Pageable pageable, ProductSearchParams searchParams);
    ProductDTO getById(Integer id);
    List<ProductDTO> getAllByIds(Collection<Integer> ids);
    Page<SimpleDetailedProductDTO> getAllDetailed(Pageable pageable, ProductSearchParams searchParams);
    Page<SimpleDetailedProductDTO> getAllDetailedByCategoryId(Pageable pageable, Integer categoryId);
    DetailedProductDTO getDetailedById(Integer id);
    ProductDTO create(ProductModifiableDTO dto);
    String saveImageById(Integer id, MultipartFile image);
    ProductDTO updateById(Integer id, ProductModifiableDTO dto);
    void activateByIds(Collection<Integer> ids);
    void deactivateById(Integer id);
    void removeImageById(Integer id);
}
