package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.productservice.dto.SimplifiedProductDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.SearchParams;
import ua.com.agroswit.productservice.dto.mapper.ProductMapper;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.Product;
import ua.com.agroswit.productservice.repository.CategoryRepository;
import ua.com.agroswit.productservice.repository.ProducerRepository;
import ua.com.agroswit.productservice.repository.ProductRepository;
import ua.com.agroswit.productservice.service.ProductService;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProducerRepository producerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepo;
    private final ProductMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAll(Pageable pageable) {
        return productRepo.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAll(Pageable pageable, SearchParams searchParams) {
        Page<Product> products;
        if (searchParams.producerId() != null && searchParams.active() != null) {
           products = productRepo.findAllByActiveTrueAndProducerId(searchParams.producerId(), pageable);
        } else if (searchParams.producerId() != null) {
            products = productRepo.findAllByProducerId(searchParams.producerId(), pageable);
        } else if (searchParams.active() != null) {
            products = productRepo.findAllByActiveTrue(pageable);
        } else {
            products = productRepo.findAll(pageable);
        }

        return products.map(mapper::toDTO);
    }

    @Override
    public Page<ProductDTO> getAllBy1CIds(Collection<Integer> ids, Pageable pageable) {
        return productRepo.findAllByArticle1CIdIn(ids, pageable)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getById(Integer id) {
        return productRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Product with id %d not found", id))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getBy1CId(Integer article1CId) {
        return productRepo.findByArticle1CId(article1CId)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Product with 1C id %d not found", article1CId))
                );
    }

    @Override
    @Transactional
    public ProductDTO create(SimplifiedProductDTO dto) {
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
    @Transactional
    public ProductDTO update() {
        return null;
    }

    @Override
    @Transactional
    public void deactivate(Integer id) {
        log.info("Deactivating product with id: {}", id);
        productRepo.deactivateById(id);
    }
}
