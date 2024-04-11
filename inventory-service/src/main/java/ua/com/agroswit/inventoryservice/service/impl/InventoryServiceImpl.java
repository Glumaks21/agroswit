package ua.com.agroswit.inventoryservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final InventoryMapper mapper;
    private final WebClient.Builder webClientBuilder;

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<InventoryDTO>> getAll(Pageable pageable) {
        return inventoryRepo.findAllBy(pageable)
                .collectList()
                .zipWith(inventoryRepo.count())
                .flatMap(t2 -> {
                    var ids = t2.getT1().stream()
                            .map(Inventory::getProduct1CId)
                            .toList();

                    log.info("Sending get request to product service");
                    var productListMono = webClientBuilder.build()
                            .get()
                            .uri("http://product-service/api/v1/products?%{1c_id}", ids)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<List<ProductDTO>>() {
                            });

                    return Mono.zip(Mono.just(t2.getT1()), productListMono, Mono.just(t2.getT2()));
                })
                .map(t3 -> {
                    List<InventoryDTO> dtos = new ArrayList<>();
                    var inventories = t3.getT1();
                    var products = t3.getT2();

                    for (int i = 0; i < inventories.size(); i++) {
                        var dto = mapper.toDTO(inventories.get(i));
                        dto.setProduct(products.get(i));
                        dtos.add(dto);
                    }

                    return new PageImpl<>(dtos, pageable, t3.getT3());
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<InventoryDTO> getByIds(Collection<Integer> ids) {
        return inventoryRepo.findAllById(ids)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<InventoryDTO> getById(Integer id) {
        var inventoryMono = inventoryRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(String.format(
                        "Inventory product with 1c id %d not found", id)))
                );

        var productMono = webClientBuilder.build().get()
                .uri("http://product-service/api/v1/products?1c_id={id}", id)
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(String.format(
                                "Product with 1c id %d not found", id)
                        ))
                )
                .collectList();

        return inventoryMono.flatMap(i ->
                productMono.map(p -> {
                    var dto = mapper.toDTO(i);
                    dto.setProduct(p.getFirst());
                    return dto;
                })
        );
    }

    @Override
    @Transactional
    public Mono<InventoryDTO> save(InventoryDTO dto) {
        var inventoryProduct = mapper.toEntity(dto);

        var validationMono = inventoryRepo.existsById(dto.getProduct1CId())
                .switchIfEmpty(Mono.error(new ResourceInConflictStateException(String.format(
                                "Product with 1c id %d already exists", dto.getProduct1CId()))
                        )
                );

        var productMono = webClientBuilder.build().get()
                .uri("http://product-service/api/v1/products?1c_id={id}", dto.getProduct1CId())
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(String.format(
                                "Product with 1c id %d not found", dto.getProduct1CId()))
                        )
                )
                .collectList();

        return validationMono
                .doOnNext(p -> log.info("Fetching product info"))
                .then(productMono)
                .doOnNext(p -> log.info("Saving product to inventory db {}", inventoryProduct))
                .flatMap(p -> inventoryRepo.insert(inventoryProduct.getProduct1CId(), inventoryProduct.getQuantity())
                        .flatMap(count -> inventoryRepo.findById(inventoryProduct.getProduct1CId())
                                .map(saved -> {
                                    var inventoryDTO = mapper.toDTO(saved);
                                    inventoryDTO.setProduct(p.getFirst());
                                    return inventoryDTO;
                                })
                        )
                );
    }

    @Override
    @Transactional
    public Mono<InventoryDTO> updateById(InventoryDTO dto, Integer id) {
        var inventoryProductMono = inventoryRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(String.format(
                                "Inventory product with 1c id %d not found", id))
                        )
                );

        var productMono = webClientBuilder.build().get()
                .uri("http://product-service/api/v1/products?1c_id={id}", id)
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .collectList();

        return inventoryProductMono.flatMap(i ->
                productMono.flatMap(p -> {
                    mapper.update(dto, i);

                    log.info("Updating product in db: {}", i);
                    return inventoryRepo.update(i.getProduct1CId(), i.getQuantity())
                            .flatMap(count -> inventoryRepo.findById(i.getProduct1CId()))
                            .map(saved -> {
                                var inventoryDTO = mapper.toDTO(i);
                                inventoryDTO.setProduct(p.getFirst());
                                return inventoryDTO;
                            });
                })
        );
    }

    @Override
    @Transactional
    public Mono<Void> delete(Integer id) {
        log.info("Deleting product from inventory with 1c id: {}", id);
        return inventoryRepo.deleteById(id);
    }
}
