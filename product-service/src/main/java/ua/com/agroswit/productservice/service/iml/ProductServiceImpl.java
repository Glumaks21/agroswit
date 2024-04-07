package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import ua.com.agroswit.productservice.dto.ProductCreationDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.mapper.ProductMapper;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.repository.CategoryRepository;
import ua.com.agroswit.productservice.repository.ProducerRepository;
import ua.com.agroswit.productservice.repository.ProductRepository;
import ua.com.agroswit.productservice.service.ProductService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProducerRepository producerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepo;
    private final ProductMapper mapper;

    @Override
    public Page<ProductDTO> getAll(Pageable pageable) {
        return productRepo.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<ProductDTO> getAllActive(Pageable pageable) {
        return productRepo.findAllByActiveTrue(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<ProductDTO> getAllByProducerId(Integer producerId, Pageable pageable) {
        return productRepo.findAllByProducerId(producerId, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<ProductDTO> getAllActiveByProducerId(Integer producerId, Pageable pageable) {
        return productRepo.findAllByActiveTrueAndProducerId(producerId, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public ProductDTO getById(Integer id) {
        return productRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Product with id %d not found", id))
                );
    }

    @Transactional
    @Override
    public ProductDTO create(ProductCreationDTO dto) {
        if (productRepo.existsByArticle1CId(dto.article1CId())) {
            throw new ResourceInConflictStateException(String.format(
                    "Product with 1c id %d already exists", dto.article1CId())
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
//        product.getPackages().forEach(p -> p.setProduct(product));

        log.info("Saving product: {}", product);
        productRepo.save(product);

        return mapper.toDTO(product);
    }

    @Override
    public ProductDTO update() {
        return null;
    }

    @Override
    public void deactivate(Integer id) {
        log.info("Deactivating product with id: {}", id);
        productRepo.deactivateById(id);
    }
}
