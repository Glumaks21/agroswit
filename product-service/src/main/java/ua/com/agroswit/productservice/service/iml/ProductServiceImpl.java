package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.client.InventoryClient;
import ua.com.agroswit.productservice.dto.request.ProductModifiableDTO;
import ua.com.agroswit.productservice.dto.response.DetailedProductDTO;
import ua.com.agroswit.productservice.dto.request.ProductSearchParams;
import ua.com.agroswit.productservice.dto.mapper.ProductMapper;
import ua.com.agroswit.productservice.dto.response.ProductDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.Filter;
import ua.com.agroswit.productservice.model.Product;
import ua.com.agroswit.productservice.repository.CategoryRepository;
import ua.com.agroswit.productservice.repository.FilterRepository;
import ua.com.agroswit.productservice.repository.ProducerRepository;
import ua.com.agroswit.productservice.repository.ProductRepository;
import ua.com.agroswit.productservice.service.ProductService;

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
    private final InventoryClient inventoryClient;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAll(Pageable pageable, ProductSearchParams searchParams) {
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
        return products.map(p -> mapper.toDTO(p, uploadService.getUrl(p.getImage())));
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
                .map(p ->  mapper.toDTO(p, uploadService.getUrl(p.getImage())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SimpleDetailedProductDTO> getAllDetailed(Pageable pageable, ProductSearchParams searchParams) {
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

        var ids = products.stream().map(Product::getId).toList();
        log.trace("Requesting inventory service for product ids: {}", ids);
        var inventories = inventoryClient.getByProductIds(ids);

        return products
                .map(p -> {
                    for (var i : inventories) {
                        if (i.productId().equals(p.getId())) {
                            return mapper.toSimpleDetailedDTO(p, i, uploadService.getUrl(p.getImage()));
                        }
                    }
                    return mapper.toSimpleDetailedDTO(p, null, uploadService.getUrl(p.getImage()));
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SimpleDetailedProductDTO> getAllDetailedByCategoryId(Pageable pageable, Integer categoryId) {
        var products = productRepo.findAllByCategoryId(categoryId, pageable);

        var ids = products.stream().map(Product::getId).toList();
        log.trace("Requesting inventory service for product ids: {}", ids);
        var inventories = inventoryClient.getByProductIds(ids);

        return products
                .map(p -> {
                    for (var i : inventories) {
                        if (i.productId().equals(p.getId())) {
                            return mapper.toSimpleDetailedDTO(p, i, uploadService.getUrl(p.getImage()));
                        }
                    }
                    return mapper.toSimpleDetailedDTO(p, null, uploadService.getUrl(p.getImage()));
                });
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
        var inventory = inventoryClient.getByProductIds(List.of(id));
        if (inventory.isEmpty()) {
            throw new ResourceNotFoundException(String.format(
                    "Inventory records not found for product id: %d", id)
            );
        }

        return mapper.toDetailedDTO(product, inventory.getFirst(), uploadService.getUrl(product.getImage()));
    }

    @Override
    @Transactional
    public ProductDTO create(ProductModifiableDTO dto) {
        var product = mapper.toEntity(dto);
        fetchRelations(product, dto);

        log.info("Saving product to db: {}", product);
        var savedProduct = productRepo.save(product);

        return mapper.toDTO(savedProduct, uploadService.getUrl(savedProduct.getImage()));
    }

    @Override
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
        var savedProduct = productRepo.save(product);
        return uploadService.getUrl(savedProduct.getImage());
    }

    private void fetchRelations(Product product, ProductModifiableDTO dto) {
        fetchProducer(product, dto);
        fetchCategory(product, dto);
        fetchFilters(product, dto);
    }

    private void fetchProducer(Product product, ProductModifiableDTO dto) {
        var producer = product.getProducer();
        if (dto.getProducerId() != null) {
            if (producer == null || !producer.getId().equals(dto.getProducerId())) {
                producer = producerRepo.findById(dto.getProducerId())
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(
                                "Producer with id %d not found", dto.getProducerId()))
                        );
            }
        }
        product.setProducer(producer);
    }

    private void fetchCategory(Product product, ProductModifiableDTO dto) {
        var category = product.getCategory();
        if (dto.getCategoryId() != null) {
            if (category == null || !category.getId().equals(dto.getCategoryId())) {
                category = categoryRepo.findById(dto.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(
                                "Category with id %d not found", dto.getCategoryId()))
                        );
            }
        }
        product.setCategory(category);
    }

    private void fetchFilters(Product product, ProductModifiableDTO dto) {
        var filters = product.getFilters();
        if (dto.getFilterIds() != null && !dto.getFilterIds().isEmpty()) {
            var count = filterRepo.countByIdIn(dto.getFilterIds());

            if (count != dto.getFilterIds().size()) {
                throw new ResourceNotFoundException(String.format(
                        "Some filters not found in list: %s" , dto.getFilterIds())
                );
            }

            filters = dto.getFilterIds().stream()
                    .map(id -> {
                        var filter = new Filter();
                        filter.setId(id);
                        return filter;
                    })
                    .toList();
        }

        product.setFilters(filters);
    }

    @Override
    @Transactional
    public ProductDTO updateById(Integer id, ProductModifiableDTO dto) {
        var product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Product with id %d not found", id))
                );


        log.trace("Removing property groups for product with id: {}", id);
        productRepo.deletePropertyGroupsById(id);
        mapper.fullUpdate(dto, product);
        fetchRelations(product, dto);
        log.info("Updating product in db: {}", product);
        productRepo.save(product);

        return mapper.toDTO(product, uploadService.getUrl(product.getImage()));
    }

    @Override
    @Transactional
    public void deactivateById(Integer id) {
        log.info("Deactivating product with id: {}", id);
        productRepo.updateActiveByIds(List.of(id), false);
    }

    @Override
    @Transactional
    public void activateByIds(Collection<Integer> ids) {
        log.info("Activating products with ids: {}", ids);
        productRepo.updateActiveByIds(ids, true);
    }

    @Override
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