package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.client.InventoryClient;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.dto.mapper.ProducerMapper;
import ua.com.agroswit.productservice.dto.mapper.ProductMapper;
import ua.com.agroswit.productservice.dto.request.ProductSearchParams;
import ua.com.agroswit.productservice.dto.response.DetailedProductDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.Filter;
import ua.com.agroswit.productservice.model.Product;
import ua.com.agroswit.productservice.repository.CategoryRepository;
import ua.com.agroswit.productservice.repository.FilterRepository;
import ua.com.agroswit.productservice.repository.ProducerRepository;
import ua.com.agroswit.productservice.repository.ProductRepository;
import ua.com.agroswit.productservice.service.ProductService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProducerRepository producerRepo;
    private final CategoryRepository categoryRepo;
    private final FilterRepository filterRepo;
    private final ProductRepository productRepo;
    private final MinioUploadService uploadService;
    private final ProductMapper mapper;
    private final ProducerMapper producerMapper;
    private final InventoryClient inventoryClient;


    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAll(Pageable pageable, ProductSearchParams searchParams) {
        return productRepo.findAll(createSpecification(searchParams), pageable)
                .map(p -> mapper.toDTO(p, uploadService.getUrl(p.getImage())));
    }

    private Specification<Product> createSpecification(ProductSearchParams searchParams) {
        Specification<Product> spec = Specification.where(null);

        if (searchParams.active() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("active"), searchParams.active()));
        }
        if (searchParams.producer_id() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                var producerJoin = root.join("producer");
                return criteriaBuilder.equal(producerJoin.get("id"), searchParams.producer_id());
            });
        }
        if (searchParams.f_id() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                var filterJoin = root.join("filters");
                return criteriaBuilder.in(filterJoin.get("id")).value(searchParams.f_id());
            });
        }
        return spec;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getById(Integer id) {
        return productRepo.findById(id)
                .map(p -> mapper.toDTO(p, uploadService.getUrl(p.getImage())))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with id %d not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllByIds(Collection<Integer> ids) {
        return productRepo.findAllById(ids).stream()
                .map(p -> mapper.toDTO(p, uploadService.getUrl(p.getImage())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SimpleDetailedProductDTO> getAllDetailed(Pageable pageable, ProductSearchParams searchParams) {
        var page = productRepo.findAll(createSpecification(searchParams), pageable);
        var content = fetchDetails(page.getContent());
        return new PageImpl<>(content, pageable, content.size());
    }

    private List<SimpleDetailedProductDTO> fetchDetails(Collection<Product> products) {
        var ids = products.stream().map(Product::getId).toList();
        log.trace("Requesting inventory records service for product ids: {}", ids);
        var inventories = inventoryClient.getByProductIds(ids);
        return products.stream()
                .map(p -> {
                    for (var i : inventories) {
                        if (i.productId().equals(p.getId())) {
                            return mapper.toSimpleDetailedDTO(p, i, uploadService.getUrl(p.getImage()));
                        }
                    }
                    return mapper.toSimpleDetailedDTO(p, uploadService.getUrl(p.getImage()));
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SimpleDetailedProductDTO> getAllDetailedByCategoryId(Pageable pageable, Integer categoryId) {
        var page = productRepo.findAllByCategoryId(categoryId, pageable);
        var content = fetchDetails(page.getContent());
        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProducerDTO> getAllProducersOfProductsByCategoryById(Integer categoryId) {
        return productRepo.findAllProducersOfProductsByCategoryId(categoryId).stream()
                .map(p -> producerMapper.toDTO(p, uploadService.getUrl(p.getLogo())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SimpleDetailedProductDTO> getAllDetailedByProducerId(Integer producerId, Pageable pageable) {
        var page = productRepo.findAllByProducerId(producerId, pageable);
        var content = fetchDetails(page.getContent());
        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    @Transactional(readOnly = true)
    public DetailedProductDTO getDetailedById(Integer id) {
        var productOptional = productRepo.findById(id);
        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Product with id %d not found", id));
        }
        var product = productOptional.get();

        log.trace("Requesting inventory service for product id: {}", id);
        var inventories = inventoryClient.getByProductIds(List.of(id));
        if (!inventories.isEmpty()) {
            return mapper.toDetailedDTO(product, inventories.getFirst(), uploadService.getUrl(product.getImage()));

        }

        return mapper.toDetailedDTO(product, uploadService.getUrl(product.getImage()));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        var product = mapper.toEntity(dto);
        product.getPackages().forEach(p -> p.setOldPrice(p.getPrice()));
        fetchRelations(product, dto);

        log.info("Saving product to db: {}", product);
        var savedProduct = productRepo.save(product);

        return mapper.toDTO(savedProduct, uploadService.getUrl(savedProduct.getImage()));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public String saveImageById(Integer id, MultipartFile image) {
        var product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Product with id %d not found", id))
                );

        log.trace("Saving image to storage: {}", image.getOriginalFilename());
        var imageName = uploadService.uploadImage(image);

        if (product.getImage() != null) {
            log.trace("Removing previous product image: {}", product.getImage());
            uploadService.remove(product.getImage());
        }

        product.setImage(imageName);
        log.info("Saving product with new logo to db: {}", product);
        productRepo.save(product);
        return uploadService.getUrl(imageName);
    }

    private void fetchRelations(Product product, ProductDTO dto) {
        fetchProducer(product, dto);
        fetchCategory(product, dto);
        fetchFilters(product, dto);
    }

    private void fetchProducer(Product product, ProductDTO dto) {
        var producer = product.getProducer();
        if (dto.producerId() != null) {
            if (producer == null || !producer.getId().equals(dto.producerId())) {
                producer = producerRepo.findById(dto.producerId())
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(
                                "Producer with id %d not found", dto.producerId()))
                        );
            }
        } else {
            producer = null;
        }
        product.setProducer(producer);
    }

    private void fetchCategory(Product product, ProductDTO dto) {
        var category = product.getCategory();
        if (dto.categoryId() != null) {
            if (category == null || !category.getId().equals(dto.categoryId())) {
                category = categoryRepo.findById(dto.categoryId())
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(
                                "Category with id %d not found", dto.categoryId()))
                        );
            }
        } else {
            category = null;
        }
        product.setCategory(category);
    }

    private void fetchFilters(Product product, ProductDTO dto) {
        var filters = new ArrayList<Filter>();
        if (dto.filterIds() != null && !dto.filterIds().isEmpty()) {
            var count = filterRepo.countByIdIn(dto.filterIds());

            if (count != dto.filterIds().size()) {
                throw new ResourceNotFoundException(String.format(
                        "Some filters not found in list: %s", dto.filterIds())
                );
            }

            dto.filterIds().forEach(id -> {
                var filter = new Filter();
                filter.setId(id);
                filters.add(filter);
            });
        }
        product.getFilters().clear();
        product.getFilters().addAll(filters);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductDTO fullUpdateById(Integer id, ProductDTO dto) {
        var product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found".formatted(id)));

        log.trace("Removing property groups for product with id: {}", id);
        productRepo.deleteAllPropertyGroupsById(id);
//        product.getPackages().forEach(p -> p.setOldPrice(p.getPrice()));
        mapper.fullUpdate(dto, product);
        fetchRelations(product, dto);
        log.info("Updating product in db: {}", product);
        productRepo.save(product);

        return mapper.toDTO(product, uploadService.getUrl(product.getImage()));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductDTO partialUpdateById(Integer id, ProductDTO dto) {
        var product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Product with id %d not found", id))
                );

        if (dto.propertyGroups() != null && !dto.propertyGroups().isEmpty()) {
            log.trace("Removing property groups for product with id: {}", id);
            productRepo.deleteAllPropertyGroupsById(id);
        }

        mapper.partialUpdate(dto, product);
        fetchRelations(product, dto);
        log.info("Updating product in db: {}", product);
        productRepo.save(product);

        return mapper.toDTO(product, uploadService.getUrl(product.getImage()));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deactivateById(Integer id) {
        log.info("Deactivating product with id: {}", id);
        productRepo.updateActiveByIds(List.of(id), false);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void activateByIds(Collection<Integer> ids) {
        log.info("Activating products with ids: {}", ids);
        productRepo.updateActiveByIds(ids, true);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void removeImageById(Integer id) {
        var product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Product with id %d not found", id))
                );

        if (product.getImage() != null) {
            log.trace("Removing image for product id {} from storage: {}", id, product.getImage());
            uploadService.remove(product.getImage());

            product.setImage(null);
            log.info("Updating product without image to db: {}", product);
            productRepo.save(product);
        }
    }
}