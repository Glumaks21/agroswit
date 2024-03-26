package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.dto.request.ProductCreationDTO;
import ua.com.agroswit.dto.response.ProductDTO;
import ua.com.agroswit.dto.response.mappers.ProductMapper;
import ua.com.agroswit.exception.ResourceInConflictStateException;
import ua.com.agroswit.exception.ResourceNotFoundException;
import ua.com.agroswit.model.Product;
import ua.com.agroswit.repository.CategoryRepository;
import ua.com.agroswit.repository.PackageRepository;
import ua.com.agroswit.repository.ProducerRepository;
import ua.com.agroswit.repository.ProductRepository;
import ua.com.agroswit.service.ProductService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProducerRepository producerRepository;
    private final CategoryRepository categoryRepository;
    private final PackageRepository packageRepository;
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public Page<ProductDTO> getAll(Pageable pageable) {
        var productPage = productRepository.findAll(pageable);

        return productPage.map(mapper::toDTO);
    }

    @Override
    public Page<ProductDTO> getAllByProducer(Integer producerId, Pageable pageable) {
        var productPage = productRepository.findByProducerId(producerId, pageable);
        return productPage.map(mapper::toDTO);
    }

    @Override
    public Optional<ProductDTO> getById(Integer id) {
        var product = productRepository.findById(id);
        System.out.println(product);
        return product.map(mapper::toDTO);
    }

    @Override
    public Optional<ProductDTO> getByName(String name) {
        var product = productRepository.findByName(name);
        return product.map(mapper::toDTO);
    }

    @Transactional
    @Override
    public ProductDTO create(ProductCreationDTO dto) {
        if (productRepository.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Product with name %s already exists", dto.name())
            );
        }

        var producer = producerRepository.findById(dto.producerId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with id %d not found", dto.producerId()))
                );
        var category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not exists", dto.categoryId()))
                );

        var product = mapper.toEntity(dto);
        product.setProducer(producer);
        product.setCategory(category);

        log.info("Saving product: {}", product);
        productRepository.save(product);

        product.getPackages().forEach(p -> p.setProductId(product.getId()));

        log.info("Saving product packages: {}", product);
        packageRepository.saveAll(product.getPackages());

        return mapper.toDTO(product);
    }

    @Override
    public ProductDTO update() {
        return null;
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting product with id: ${}", id);
        productRepository.deleteById(id);
    }
}
