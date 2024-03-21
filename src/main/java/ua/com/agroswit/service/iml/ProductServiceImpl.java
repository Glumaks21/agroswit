package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.agroswit.dto.request.ProductCreationDTO;
import ua.com.agroswit.dto.response.ProductDTO;
import ua.com.agroswit.dto.response.converter.ProductDTOConverter;
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
    private final ProductDTOConverter converter;

    @Override
    public Page<ProductDTO> getAll(Pageable pageable) {
        var productPage = productRepository.findAll(pageable);

        return productPage.map(p -> converter.convert(
                p, productRepository.findAllPropertiesById(p.getId())
        ));
    }

    @Override
    public Page<ProductDTO> getAllByProducer(Integer producerId, Pageable pageable) {
        var productPage = productRepository.findByProducerId(producerId, pageable);
        return productPage.map(p -> converter.convert(
                p, productRepository.findAllPropertiesById(p.getId())
        ));
    }

    @Override
    public Optional<ProductDTO> getById(Long id) {
        var product = productRepository.findById(id);

        System.out.println(productRepository.findAllPropertiesById(id));
        return product.map((p) -> converter.convert(
                p, productRepository.findAllPropertiesById(id)));
    }

    @Override
    public Optional<ProductDTO> getByName(String name) {
        var product = productRepository.findByName(name);

        return product.map((p) -> converter.convert(
                p, productRepository.findAllPropertiesById(p.getId())
        ));
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
