package ua.com.agroswit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.model.Product;

import java.util.Optional;

public interface ProductService {
    Page<Product> getAll(Pageable pageable);
    Optional<Product> getById(Long id);
    Optional<Product> getByName(String name);
    Product create();
    Product update();
    void delete(Long id);
}
