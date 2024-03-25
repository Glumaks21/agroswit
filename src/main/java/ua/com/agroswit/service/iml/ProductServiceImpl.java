package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.agroswit.dto.request.ProductCreationDTO;
import ua.com.agroswit.dto.response.ProductDTO;
import ua.com.agroswit.dto.response.converter.ProductDTOConverter;
import ua.com.agroswit.exception.ResourceInConflictStateException;
import ua.com.agroswit.exception.ResourceNotFoundException;
import ua.com.agroswit.model.Package;
import ua.com.agroswit.model.Product;
import ua.com.agroswit.repository.ProducerRepository;
import ua.com.agroswit.repository.ProductRepository;
import ua.com.agroswit.service.ProductService;

import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<ProductDTO> getById(Integer id) {
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
        if (productRepository.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Product with name %s already exists", dto.name())
            );
        }

        var producer = producerRepository.findById(dto.producerId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with id %d not exists", dto.producerId()))
                );

        var packages = dto.packages().stream()
                .map(m -> Package.builder()
                        .price(m.price())
                        .volume(m.volume())
                        .unit(m.unit())
                        .build())
                .collect(Collectors.toSet());

        var product = Product.builder()
                .name(dto.name())
                .packages(packages)
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
    public void delete(Integer id) {
        log.info("Deleting product with id: ${}", id);
        productRepository.deleteById(id);
    }
}
