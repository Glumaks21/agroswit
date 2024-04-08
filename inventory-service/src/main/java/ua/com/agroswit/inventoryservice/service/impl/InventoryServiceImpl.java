package ua.com.agroswit.inventoryservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import ua.com.agroswit.inventoryservice.exception.ResourceInConflictStateException;
import ua.com.agroswit.inventoryservice.exception.ResourceNotFoundException;
import ua.com.agroswit.inventoryservice.dto.InventoryDTO;
import ua.com.agroswit.inventoryservice.dto.ProductDTO;
import ua.com.agroswit.inventoryservice.dto.mapper.InventoryMapper;
import ua.com.agroswit.inventoryservice.model.Inventory;
import ua.com.agroswit.inventoryservice.repository.InventoryRepository;
import ua.com.agroswit.inventoryservice.service.InventoryService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final InventoryMapper mapper;
    private final RestClient.Builder restClientBuilder;

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryDTO> getAll(Pageable pageable) {
        var inventoryPage = inventoryRepo.findAll(pageable);

        var product1CIds = inventoryPage
                .map(Inventory::getProduct1CId)
                .toList();
        var productEntity = restClientBuilder.build().get()
                .uri("http://product-service/api/v1/products?%{1c_id}", product1CIds)
                .retrieve();

        if (inventoryPage.getTotalElements() == 1) {
            var product = productEntity.body(ProductDTO.class);
            return inventoryPage.map(mapper::toDTO)
                    .map(i -> {
                        i.setProduct(product);
                        return i;
                    });
        }

        var products = productEntity.body(new ParameterizedTypeReference<List<ProductDTO>>() {
        });
        var inventoryProducts = inventoryPage.getContent();
        var inventoryProductDTOs = new ArrayList<InventoryDTO>(inventoryProducts.size());
        for (int i = 0; i < inventoryProducts.size(); i++) {
            var inventoryDTO = mapper.toDTO(inventoryProducts.get(i));
            inventoryDTO.setProduct(products.get(i));
            inventoryProductDTOs.add(inventoryDTO);
        }

        return new PageImpl<>(inventoryProductDTOs, inventoryPage.getPageable(), inventoryPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryDTO getById(Integer id) {
        var inventoryProduct = inventoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Inventory product with 1c id %d not found", id))
                );

        var productEntity = restClientBuilder.build().get()
                .uri("http://product-service/api/v1/products?1c_id={id}", inventoryProduct.getProduct1CId())
                .retrieve()
                .toEntity(ProductDTO.class);

        var inventoryDTO = mapper.toDTO(inventoryProduct);
        inventoryDTO.setProduct(productEntity.getBody());

        return inventoryDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDTO> getByIds(Collection<Integer> ids) {
        return inventoryRepo.findAllById(ids).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public InventoryDTO save(InventoryDTO dto) {
        var product = mapper.toEntity(dto);

        if (inventoryRepo.existsById(dto.getProduct1CId())) {
            throw new ResourceInConflictStateException(String.format(
                    "Product with 1c id %d already exists", dto.getProduct1CId())
            );
        }

        var productEntity = restClientBuilder.build().get()
                .uri("http://product-service/api/v1/products?1c_id={id}", dto.getProduct1CId())
                .retrieve()
                .toEntity(ProductDTO.class);
        //TODO add server communication exception
        if (productEntity.getStatusCode().isSameCodeAs(NOT_FOUND)) {
            throw new ResourceNotFoundException(String.format(
                    "Product with 1c id %d not found", dto.getProduct1CId())
            );
        }

        log.info("Saving product to db: {}", product);
        inventoryRepo.save(product);

        var savedDTO = mapper.toDTO(product);
        savedDTO.setProduct(productEntity.getBody());
        return savedDTO;
    }

    @Override
    @Transactional
    public InventoryDTO updateById(InventoryDTO dto, Integer id) {
        var inventoryProduct = inventoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Inventory product with 1c id %d not found", id))
                );

        log.info("Updating product in db: {}", inventoryProduct);
        mapper.update(dto, inventoryProduct);

        var productEntity = restClientBuilder.build().get()
                .uri("http://product-service/api/v1/products?1c_id={id}", inventoryProduct.getProduct1CId())
                .retrieve()
                .toEntity(ProductDTO.class);

        var inventoryDTO = mapper.toDTO(inventoryProduct);
        inventoryDTO.setProduct(productEntity.getBody());

        return inventoryDTO;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting product from inventory with 1c id: {}", id);
        inventoryRepo.deleteById(id);
    }
}
