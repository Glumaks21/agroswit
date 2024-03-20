package ua.com.agroswit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.agroswit.model.Product;
import ua.com.agroswit.repository.ProductRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public Page<Product> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Product> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Product> getByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Product create() {
        return null;
    }

    @Override
    public Product update() {
        return null;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting product with id: ${id}", id);
        repository.deleteById(id);
    }
}
