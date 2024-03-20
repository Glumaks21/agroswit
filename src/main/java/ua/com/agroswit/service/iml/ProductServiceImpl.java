package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.agroswit.dto.request.ProductCreationDTO;
import ua.com.agroswit.model.Product;
import ua.com.agroswit.repository.ProducerRepository;
import ua.com.agroswit.repository.ProductRepository;
import ua.com.agroswit.service.ProductService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProducerRepository producerRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<Product> getAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getAllByProducer(Integer producerId, Pageable pageable) {
        return productRepository.findByProducerId(producerId, pageable);
    }

    @Override
    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> getByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Product create(ProductCreationDTO dto) {
        var producer = producerRepository.findById(dto.producerId())
                .orElseThrow(() -> new RuntimeException("Producer with id " + dto.producerId() + " not exists"));
        var product = Product.builder()
                .name(dto.name())
                .price(dto.price())
                .producer(producer)
                .build();

        log.info("Saving product: {}", product);
        return productRepository.save(product);
    }

    @Override
    public Product update() {
        return null;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting product with id: ${}", id);
        productRepository.deleteById(id);
    }
}
